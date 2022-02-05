/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.enterprise.admin.servermgmt.cli;

import static com.sun.enterprise.admin.cli.CLIConstants.DEATH_TIMEOUT_MS;
import static com.sun.enterprise.admin.cli.CLIConstants.MASTER_PASSWORD;
import static com.sun.enterprise.admin.cli.CLIConstants.WAIT_FOR_DAS_TIME_MS;
import static com.sun.enterprise.util.StringUtils.ok;
import static com.sun.enterprise.util.net.NetUtils.isRunning;
import static java.util.logging.Level.FINER;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.glassfish.api.admin.CommandException;

import com.sun.enterprise.admin.cli.CLIUtil;
import com.sun.enterprise.admin.cli.Environment;
import com.sun.enterprise.admin.launcher.GFLauncher;
import com.sun.enterprise.admin.launcher.GFLauncherException;
import com.sun.enterprise.admin.launcher.GFLauncherInfo;
import com.sun.enterprise.universal.i18n.LocalStringsImpl;
import com.sun.enterprise.universal.process.ProcessStreamDrainer;
import com.sun.enterprise.universal.process.ProcessUtils;
import com.sun.enterprise.util.HostAndPort;
import com.sun.enterprise.util.io.ServerDirs;
import com.sun.enterprise.util.net.NetUtils;

/**
 * Java does not allow multiple inheritance. Both StartDomainCommand and StartInstanceCommand have common code but they
 * are already in a different hierarchy of classes. The first common baseclass is too far away -- e.g. no "launcher"
 * variable, etc.
 *
 * Instead -- put common code in here and call it as common utilities This class is designed to be thread-safe and
 * IMMUTABLE
 *
 * @author bnevins
 */
public class StartServerHelper {

    // only set when actively trouble-shooting or investigating...
    private static final boolean DEBUG_MESSAGES_ON = false;
    private static final LocalStringsImpl strings = new LocalStringsImpl(StartServerHelper.class);

    private final boolean terse;
    private final GFLauncher launcher;
    private final Logger logger;
    private final File pidFile;
    private final GFLauncherInfo info;
    private final List<HostAndPort> addresses;
    private final ServerDirs serverDirs;
    private final String masterPassword;
    private final String serverOrDomainName;
    private final int debugPort;
    private final boolean isDebugSuspend;

    public StartServerHelper(Logger logger, boolean terse, ServerDirs serverDirs, GFLauncher launcher, String masterPassword) {
        this(logger, terse, serverDirs, launcher, masterPassword, false);
    }

    public StartServerHelper(Logger logger, boolean terse, ServerDirs serverDirs, GFLauncher launcher, String masterPassword,
            boolean debug) {
        this.logger = logger;
        this.terse = terse;
        this.launcher = launcher;
        info = launcher.getInfo();

        if (info.isDomain()) {
            serverOrDomainName = info.getDomainName();
        } else {
            serverOrDomainName = info.getInstanceName();
        }

        addresses = info.getAdminAddresses();
        this.serverDirs = serverDirs;
        pidFile = serverDirs.getPidFile();
        this.masterPassword = masterPassword;

        // it will be < 0 if both --debug is false and debug-enabled=false in jvm-config
        debugPort = launcher.getDebugPort();
        isDebugSuspend = launcher.isDebugSuspend();

        if (isDebugSuspend && debugPort >= 0) {
            logger.info(strings.get("ServerStart.DebuggerSuspendedMessage", "" + debugPort));
        }
    }

    public void waitForServer() throws CommandException {
        long startWait = System.currentTimeMillis();
        if (!terse) {
            // use stdout because logger always appends a newline
            System.out.print(strings.get("WaitServer", serverOrDomainName) + " ");
        }

        boolean alive = false;
        int count = 0;

        pinged: while (!timedOut(startWait)) {
            if (pidFile != null) {
                if (logger.isLoggable(FINER)) {
                    logger.finer("Check for pid file: " + pidFile);
                }
                if (pidFile.exists()) {
                    alive = true;
                    break pinged;
                }
            } else {
                // First, see if the admin port is responding
                // if it is, the DAS is up
                for (HostAndPort address : addresses) {
                    if (isRunning(address.getHost(), address.getPort())) {
                        alive = true;
                        break pinged;
                    }
                }
            }

            // Check to make sure the DAS process is still running
            // if it isn't, startup failed
            try {
                Process glassFishProcess = launcher.getProcess();
                int exitCode = glassFishProcess.exitValue();
                // uh oh, DAS died
                String sname;

                if (info.isDomain()) {
                    sname = "domain " + info.getDomainName();
                } else {
                    sname = "instance " + info.getInstanceName();
                }

                ProcessStreamDrainer psd = launcher.getProcessStreamDrainer();
                String output = psd.getOutErrString();
                if (ok(output)) {
                    throw new CommandException(strings.get("serverDiedOutput", sname, exitCode, output));
                } else {
                    throw new CommandException(strings.get("serverDied", sname, exitCode));
                }
            } catch (GFLauncherException | IllegalThreadStateException ex) {
                // should never happen or process is still alive
            }

            // Wait before checking again
            try {
                Thread.sleep(100);
                if (!terse && count++ % 10 == 0) {
                    System.out.print(".");
                }
            } catch (InterruptedException ex) {
                // don't care
            }
        }

        if (!terse) {
            System.out.println();
        }

        if (!alive) {
            String msg;
            String time = "" + WAIT_FOR_DAS_TIME_MS / 1000;
            if (info.isDomain()) {
                msg = strings.get("serverNoStart", strings.get("DAS"), info.getDomainName(), time);
            } else {
                msg = strings.get("serverNoStart", strings.get("INSTANCE"), info.getInstanceName(), time);
            }

            throw new CommandException(msg);
        }
    }

    /**
     * Run a series of commands to prepare for a launch.
     *
     * @return false if there was a problem.
     */
    public boolean prepareForLaunch() throws CommandException {

        waitForParentToDie();
        setSecurity();

        if (checkPorts() == false) {
            return false;
        }

        deletePidFile();

        return true;
    }

    public void report() {
        String logfile;

        try {
            logfile = launcher.getLogFilename();
        } catch (GFLauncherException ex) {
            logfile = "UNKNOWN"; // should never happen
        }

        int adminPort = -1;
        String adminPortString = "-1";

        try {
            if (addresses != null && addresses.size() > 0) {
                adminPort = addresses.get(0).getPort();
            }
            // To avoid having the logger do this: port = 4,848
            // so we do the conversion to a string ourselves
            adminPortString = "" + adminPort;
        } catch (Exception e) {
            // ignore
        }

        logger.info(strings.get("ServerStart.SuccessMessage", info.isDomain() ? "domain " : "instance", serverDirs.getServerName(),
                serverDirs.getServerDir(), logfile, adminPortString));

        if (debugPort >= 0) {
            logger.info(strings.get("ServerStart.DebuggerMessage", "" + debugPort));
        }
    }

    /**
     * If the parent is a GF server -- then wait for it to die. This is part of the Client-Server Restart Dance! THe dying
     * server called us with the system property AS_RESTART set to its pid
     *
     * @throws CommandException if we timeout waiting for the parent to die or if the admin ports never free up
     */
    private void waitForParentToDie() throws CommandException {
        // we also come here with just a regular start in which case there is
        // no parent, and the System Property is NOT set to anything...
        String pids = System.getProperty("AS_RESTART");

        if (!ok(pids)) {
            return;
        }

        int pid = -1;

        try {
            pid = Integer.parseInt(pids);
        } catch (Exception e) {
            pid = -1;
        }
        waitForParentDeath(pid);
    }

    private boolean checkPorts() {
        String err = adminPortInUse();

        if (err != null) {
            logger.warning(err);
            return false;
        }

        return true;
    }

    private void deletePidFile() {
        String msg = serverDirs.deletePidFile();

        if (msg != null && logger.isLoggable(FINER)) {
            logger.finer(msg);
        }
    }

    private void setSecurity() {
        info.addSecurityToken(MASTER_PASSWORD, masterPassword);
    }

    private String adminPortInUse() {
        return adminPortInUse(info.getAdminAddresses());
    }

    private String adminPortInUse(List<HostAndPort> adminAddresses) {
        // it returns a String for logging --- if desired
        for (HostAndPort addr : adminAddresses) {
            if (!NetUtils.isPortFree(addr.getHost(), addr.getPort())) {
                return strings.get("ServerRunning", Integer.toString(addr.getPort()));
            }
        }

        return null;
    }

    // use the pid we received from the parent server and platform specific tools
    // to see FOR SURE when the entire JVM process is gone. This solves
    // potential niggling bugs.
    private void waitForParentDeath(int pid) throws CommandException {
        if (pid < 0) {
            // can not happen. (Famous Last Words!)
            new ParentDeathWaiterPureJava();
            return;
        }

        long start = System.currentTimeMillis();
        try {
            do {
                Boolean b = ProcessUtils.isProcessRunning(pid);
                if (b == null) {
                    // this means we were unable to find out from the OS if the process
                    // is running or not
                    debugMessage("ProcessUtils.isProcessRunning(" + pid + ") " + "returned null which means we can't get process "
                            + "info on this platform.");

                    new ParentDeathWaiterPureJava();
                    return;
                }
                if (b.booleanValue() == false) {
                    debugMessage("Parent process (" + pid + ") is dead.");
                    return;
                }
                // else parent is still breathing...
                debugMessage("Wait one more second for parent to die...");
                Thread.sleep(1000);
            } while (!timedOut(start, DEATH_TIMEOUT_MS));

        } catch (Exception e) {
            // fall through. Normal returns are in the block above
        }

        // abnormal return path
        throw new CommandException(strings.get("deathwait_timeout", DEATH_TIMEOUT_MS));
    }

    private static boolean timedOut(long startTime) {
        return timedOut(startTime, WAIT_FOR_DAS_TIME_MS);
    }

    private static boolean timedOut(long startTime, long span) {
        return System.currentTimeMillis() - startTime > span;
    }

    private static void debugMessage(String s) {
        // very difficult to see output from this process when part of restart-domain.
        // Normally there is no console.
        // There are **three** JVMs in a restart -- old server, new server, cli
        // we will not even see AS_DEBUG!
        if (DEBUG_MESSAGES_ON) {
            Environment env = new Environment();
            CLIUtil.writeCommandToDebugLog("restart-debug", env, new String[] { "DEBUG MESSAGE FROM RESTART JVM", s }, 99999);
        }
    }

    /**
     * bnevins the restart flag is set by the RestartDomain command in the local server. The dying server has started a new
     * JVM process and is running this code. Our official parent process is the dying server. The ParentDeathWaiterPureJava
     * waits for the parent process to disappear. see RestartDomainCommand in core/kernel for more details
     */
    private class ParentDeathWaiterPureJava implements Runnable {
        @Override
        public void run() {
            try {
                // When parent process is almost dead, in.read returns -1 (EOF)
                // as the pipe breaks.

                while (System.in.read() >= 0) {
                    ;
                }
            } catch (IOException ex) {
                // ignore
            }

            // The port may take some time to become free after the pipe breaks
            while (adminPortInUse(addresses) != null) {
                ;
            }
            success = true;
        }

        private ParentDeathWaiterPureJava() throws CommandException {
            try {
                Thread deathWaiterThread = new Thread(this);
                deathWaiterThread.start();
                deathWaiterThread.join(DEATH_TIMEOUT_MS);
            } catch (Exception e) {
                // ignore!
            }

            if (!success) {
                throw new CommandException(strings.get("deathwait_timeout", DEATH_TIMEOUT_MS));
            }
        }

        boolean success = false;
    }
}