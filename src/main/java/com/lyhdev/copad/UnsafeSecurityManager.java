package com.lyhdev.copad;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;

public class UnsafeSecurityManager extends SecurityManager {
	public void checkAccept(String host, int port) {
	}

	public void checkAccess(Thread t) {
	}

	public void checkAccess(ThreadGroup g) {
	}

	public void checkAwtEventQueueAccess() {
	}

	public void checkConnect(String host, int port, Object context) {
	}

	public void checkConnect(String host, int port) {
	}

	public void checkCreateClassLoader() {
	}

	public void checkDelete(String file) {
	}

	public void checkExec(String cmd) {
	}

	public void checkExit(int status) {
	}

	public void checkLink(String lib) {
	}
}
