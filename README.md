# s3-data-update-lambda

# Assumptions:
* The source API that generates the data is RestAPI and request is "HTTP.GET". <br />
* Data is stored in s3 in a file of  json format. <br />
* File name is current epoch time. <br />
* No SSL certificates required for the get Url. <br />
* Maven build is done and jar file is created before running the terraform scripts
* AWS secret and access keys will be added in provider block
