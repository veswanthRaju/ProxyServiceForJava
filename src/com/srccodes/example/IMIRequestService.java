package com.srccodes.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HelloWorld
 */
@WebServlet("/ServletRequest")
public class IMIRequestService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public Integer CCACHE = 60 * 5 * 1000; // Limit access-control and cache
	private String defaultCallback = "console.log";
	public String path = "images";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public IMIRequestService() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String ERR = "";
		ServletContext context = getServletContext();

		response.addHeader("Access-Control-Max-Age", CCACHE.toString());
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Request-Method", "*");
		response.addHeader("Access-Control-Allow-Methods", "OPTIONS, GET");
		response.addHeader("Access-Control-Allow-Headers", "*");

		String url = request.getParameter("url");
		URL imageUrl = new URL(url);
		String imageName = url.substring(url.lastIndexOf("/") + 1);

		if (url == "" || url == null) {
			ERR = "get param url is undefined";
			context.log("get param url is undefined");
		} else {

			String folderPath = context.getRealPath(path);
			String imagePath = folderPath + "\\" + imageName;

			File downloadedFile = new File(folderPath);
			Boolean isExists = downloadedFile.exists();
			
			//Creating images folder to download the images within
			if (!isExists) {
				downloadedFile.mkdir();
				System.out.println("Images directory created at :: " + downloadedFile.getAbsolutePath());
				isExists = downloadedFile.exists();

				if (!isExists) // issue in creating the directory
				{
					ERR = "\"" + folderPath + "\" folder can not be created.";
					context.log("\"" + folderPath + "\" folder can not be created.");
				}
			}

			if (ERR == "") {
				InputStream is = imageUrl.openStream();
				OutputStream os = new FileOutputStream(folderPath + "\\" + imageName);

				byte[] b = new byte[2048];
				int length;

				// Downloading images
				while ((length = is.read(b)) != -1) {
					os.write(b, 0, length);
				}

				context.log("Images are downloaded at :" + downloadedFile.getAbsolutePath());

				// Get image MIME type
				String mimeType = context.getMimeType(imagePath);
				if (mimeType == null) {
					ERR = "Could not get MIME type";
					context.log("Could not get MIME type of " + imagePath);
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					return;
				}

				// Set content type
				response.setContentType(mimeType);
				File serverFile = new File(imagePath);
				response.setContentLength((int) serverFile.length());

				// Copy the contents of the file to the responses output stream
				FileInputStream in = new FileInputStream(serverFile);
				OutputStream out = response.getOutputStream();
				byte[] buf = new byte[1024];
				int len = 0;
				while ((len = in.read(buf)) >= 0) {
					out.write(buf, 0, len);
				}
				
				in.close();
				out.close();
				is.close();
				os.close();
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
