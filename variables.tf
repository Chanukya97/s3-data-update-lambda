variable lambda_function_name {
  description     = "s3-data-update-lambda"
  type            = string
}

variable lambda_memory {
  description     = "256 MB"
  type            = string
}

variable lambda_runtime {
  description     = "Java 11 (Correto)"
  type            = string
}

variable lambda_handler {
  description     = "com.s3.data.update.lambda.S3UpdateScheduler::handleRequest"
  type            = string
}

variable region {
  description     = "ap-southeast-2"
  type            = string
}

variable version {
  description     = "1.0.0"
  type            = string
}

variable schedule {
  description     = "rate (1 hour)"
  type            = string
}