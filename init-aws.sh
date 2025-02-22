#!/bin/sh

export HTTP_PROXY=
export HTTPS_PROXY=

awslocal sqs create-queue --queue-name work-distribution