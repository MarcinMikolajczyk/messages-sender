# Message Sender

The program requires the Cassandra database to run. The database must be listening on port 9042.

To run the application, first you need to pull cassandra database from docker hub:
`docker pull cassandra`
Data should be stored in a volume,
you can change the directory where you want to store the data by modifying the value `volumes:`
in the docker-compose.yaml file. Create a key-space for messages-sender, by default in
the docker-compose.yaml file the key-space is called "practice".

Then build the image using the Dockerfile:
`docker build -t messages-sender .`

To run our application together with the database using docker-compose use the command:
`docker-compose up -d cassandra`
Then use a similar command:
`docker-compose -d up messages-sender`

## Messages

### POST

To save the message, we send JSON:

```json
{
    "email": "email address were we wont to send messages",
    "title": "title of email messages",
    "content": "text of our email",
    "magic_number": "magic number"
}
```
On the address:

- `/api/messages`

To send the messages, we send JSON:

```json
{
    "magic_number": "magic number"
}
```

On the address:

- `/api/send`

### GET
- `/api/messages/{email}?page=&size=` -  returns list of messages with email as a path variable
