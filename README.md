# ProxyServiceForJava
Used to load the external images and saved in server instance and send back to the UI. Reference for UI is https://github.com/brcontainer/html2canvas-asp-vbscript-proxy

We faced in issue while importing images (which we are loading from external URl) to pdf. In the pdf we unable to see the images.

For that we came into a approach called **proxy**

Here the sample java proxy to implement the same.

Now as the images are in response object we can load images in pdf (having in iframe)
