variable lambda_function_name {
  default     = "s3-data-update-lambda"
  type            = string
}

variable lambda_memory {
  default     = "256 MB"
  type            = string
}

variable lambda_runtime {
  default     = "Java 11 (Correto)"
  type            = string
}

variable lambda_handler {
  default     = "com.s3.data.update.lambda.S3UpdateScheduler::handleRequest"
  type            = string
}

variable region {
  default     = "ap-southeast-2"
  type            = string
}

variable version {
  default     = "1.0.0"
  type            = string
}

variable schedule {
  default     = "rate (1 hour)"
  type            = string
}
