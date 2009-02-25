package org.erlide.runtime.backend.internal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.erlide.core.util.ErlideUtil;
import org.erlide.runtime.ErlLogger;
import org.erlide.runtime.IDisposable;
import org.erlide.runtime.backend.Backend;
import org.erlide.runtime.backend.ErtsProcess;
import org.erlide.runtime.backend.RuntimeInfo;

public class ManagedLauncher implements RuntimeLauncher, IDisposable {

	Process fRuntime;
	Backend backend;

	public ManagedLauncher() {
	}

	public void setBackend(Backend backend) {
		this.backend = backend;
	}

	public void connect() {
		backend.doConnect(backend.getName());
	}

	public void stop() {
		if (fRuntime != null) {
			fRuntime.destroy();
		}
	}

	public void dispose() {
		stop();
	}

	public void initializeRuntime(ILaunch launch) {
		startRuntime(launch);
	}

	IStreamsProxy streamsProxy;
	private ErtsProcess erts;

	private void startRuntime(ILaunch launch) {
		final RuntimeInfo info = backend.getInfo();
		if (info == null) {
			ErlLogger.error("Trying to start backend '%s' with null info",
					backend.getName());
			return;
		}

		String cmd = info.getCmdLine();

		ErlLogger.debug("START node :> " + cmd);
		final File workingDirectory = new File(info.getWorkingDir());
		try {
			fRuntime = Runtime.getRuntime().exec(cmd, null, workingDirectory);
			Runnable watcher = new Runnable() {
				@SuppressWarnings("boxing")
				public void run() {
					try {
						int v = fRuntime.waitFor();
						final String msg = "Backend '%s' terminated with exit code %d.";
						ErlLogger.error(msg, info.getNodeName(), v);

						if ((v != 0) && ErlideUtil.isEricssonUser()) {
							String plog = ErlideUtil.fetchPlatformLog();
							String elog = ErlideUtil.fetchErlideLog();
							String delim = "\n==================================\n";
							File report = new File(ErlideUtil.getLocation());
							try {
								report.createNewFile();
								OutputStream out = new FileOutputStream(report);
								PrintWriter pw = new PrintWriter(out);
								try {
									pw.println(msg);
									pw.println(System.getProperty("user.name"));
									pw.println(delim);
									pw.println(plog);
									pw.println(delim);
									pw.println(elog);
								} finally {
									pw.flush();
									out.close();
								}
							} catch (IOException e) {
								ErlLogger.warn(e);
							}

						}

						backend.setExitStatus(v);
					} catch (InterruptedException e) {
						ErlLogger.warn("Backend watcher was interrupted");
					}
				}
			};
			final Thread thread = new Thread(null, watcher, "Backend watcher");
			thread.setDaemon(false);
			thread.start();

			if (launch != null) {
				erts = new ErtsProcess(launch, fRuntime, info.getNodeName(),
						null);
				launch.addProcess(erts);
			}
		} catch (final IOException e) {
			ErlLogger.error(e);
		}

		// streamsProxy = new StreamsProxy(fRuntime, null);
	}

}
