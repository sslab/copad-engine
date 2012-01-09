package com.lyhdev.copad;

import java.io.*;
import java.net.*;
import javax.script.*;
import groovy.lang.*;

public class ScriptLoader
{
	public ScriptLoader() {
	}

	public static void main(String args[])
	{
		System.setSecurityManager(new UnsafeSecurityManager());

		ScriptLoader loader = new ScriptLoader();
		try {	
			if ("groovy".equals(System.getProperty("core.engine.type"))) {
				loader.loadGroovy(System.getProperty("core.engine.url"));
			}
			else {
				loader.load(
					System.getProperty("core.engine.type"),
					System.getProperty("core.engine.url")
				);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();

			String contentType = "text/plain";
			String stdout = "";
			String stderr = ex.getMessage();
			try {
				loader.callback(contentType, stdout, stderr);
			}
			catch (Exception ex2) {
				ex2.printStackTrace();
			}
		}
		//System.exit(0);
	}

	public void load(String scriptType, String scriptURL) throws Exception {
		URL url = new URL(scriptURL);
		URLConnection conn = url.openConnection();
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(conn.getInputStream(), "UTF-8")
		);

		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName(scriptType);
		engine.put("loader", this);
		engine.eval(reader);
		reader.close();
	}

	private GroovyShell shell = null;

	public void loadGroovy(String scriptURL) throws Exception {
		System.out.println("Load Groovy " + scriptURL);

		//ClassLoader parent = getClass().getClassLoader();
		//GroovyClassLoader loader = new GroovyClassLoader(parent);
		//Class groovyClass = loader.parseClass(new File("src/test/groovy/script/HelloWorld.groovy"));

		URL url = new URL(scriptURL);
		URLConnection conn = url.openConnection();
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(conn.getInputStream(), "UTF-8")
		);

		if (shell==null) {
			Binding binding = new Binding();
			binding.setVariable("loader", this);
			shell = new GroovyShell(binding);
		}
		shell.evaluate(reader);
		reader.close();
	}

	public void callback(String contentType, String stdout, String stderr) throws Exception {
		String callbackURL = System.getProperty("core.callback.url");

		URL url = new URL(callbackURL);
		URLConnection conn = url.openConnection();
		conn.setDoOutput(true);
		OutputStreamWriter out = new OutputStreamWriter(
			conn.getOutputStream()
		);
		out.write("content_type=" + URLEncoder.encode(contentType, "UTF-8"));
		out.write("&stdout=" + URLEncoder.encode(stdout, "UTF-8"));
		out.write("&stderr=" + URLEncoder.encode(stderr, "UTF-8"));
		out.close();

		BufferedReader in = new BufferedReader(
			new InputStreamReader(conn.getInputStream())
		);

		String decodedString;

		while ((decodedString = in.readLine()) != null) {
			System.out.println(decodedString);
		}
		in.close();
	}
}
