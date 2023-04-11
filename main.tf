provider "aws" {

  region = var.region
  
}



resource "aws_iam_role" "lambda_role" {
  name = "lambda_role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Sid    = ""
        Principal = {
          Service = "lambda.amazonaws.com"
        }
      },
    ]
  })
}

resource "aws_iam_role_policy" "lambda_policy" {
  name = "lambda_policy"
  role = aws_iam_role.lambda_role.id
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = [
          "logs:*",
        ]
        Effect   = "Allow"
        Resource = "*"
      },
      {
        Action = [
          "s3:*",
        ]
        Effect   = "Allow"
        Resource = "*"
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "attach-iam-policy-to-iam-role" {
  role       = aws_iam_role.lambda_role.name
  policy_arn = aws_iam_role_policy.lambda_policy.arn
}

locals {

    lambda_payload_filename = "./target/${var.lambda_name}-${var.version}.jar"
}


resource "aws_lambda_function" "s3-scheduler-lambda" {
  filename                  = "s3-data-update-lambda"
  function_name             = var.lambda_function_name
  role                      = aws_iam_role.lambda_role.arn
  handler                   = var.lambda_handler
  runtime                   = var.lambda_runtime
  memory_size               = var.lambda_memory
  source_code_hash          = "${base64sha256(filebase64(local.lambda_payload_filename))}"
  environment { 
    variables   =  {
	region = var.region
	bucketName = var.bucketName
	dataUrl = var.dataUrl
     }
  }
}

resource "aws_cloudwatch_event_rule" "every_one_hour" {
  name = "once-every-hour"
  description = "run once a hour"
  schedule_expression = var.schedule
}

resource "aws_cloudwatch_event_target" "schedule" {
    rule = "${aws_cloudwatch_event_rule.every_one_hour.name}"
    target_id = aws_lambda_function.s3-scheduler-lambda.id
    arn = "${aws_lambda_function.s3-scheduler-lambda.arn}"
}

resource "aws_lambda_permission" "allow_cloudwatch_to_call_lambda" {
    statement_id = "AllowExecutionFromCloudWatch"
    action = "lambda:InvokeFunction"
    function_name = "${aws_lambda_function.s3-scheduler-lambda.function_name}"
    principal = "events.amazonaws.com"
    source_arn = "${aws_cloudwatch_event_rule.every_one_hour.arn}"
}