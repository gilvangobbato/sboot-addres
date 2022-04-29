# sboot-addresses
Spring Boot project to manage Brazilian addresses

## Create DynamoDB Table

```bash
aws --endpoint-url=http://localhost:4566 dynamodb create-table \
    --table-name Address \
    --attribute-definitions \
          AttributeName=cep,AttributeType=S \
    --key-schema \
          AttributeName=cep,KeyType=HASH \
    --provisioned-throughput \
          ReadCapacityUnits=15,WriteCapacityUnits=15
```

## SQS queue
To create a queue run the following command:
```bash
aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name viaCepInsert
```
The command above is to listen the queue
```bash
aws --endpoint-url=http://localhost:4566 sqs receive-message --queue-url http://localhost:4566/000000000000/viaCepInsert
```
