# Message Sender

The program requires the Cassandra database to run. The database must be listening on port 9042.

## Messages

### POST

To save the message, we send JSON:

```json
{
    "email": "email address were we wont to send messages",
    "title": "title of email messages",
    "content": "text of our email",
    "magic_number": magic number
}
```
On the address:

- `/api/messages`

To send the messages, we send JSON:

```json
{
    "magic_number": magic number
}
```

On the address:

- `/api/send`

### GET
- `/api/messages/{email}?page=&size=` -  returns list of messages with email as a path variable

