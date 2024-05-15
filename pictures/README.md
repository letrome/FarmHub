# Usage

## Prerequisites

You should have go installed on the instance you want to run this service. If this is not the case, I
recommend you to have a look at [the Go Docs](https://go.dev/doc/install).

This service has been developed in using `go1.22.3 darwin/arm64`.

Once installed, from the pictures repository, install the dependency (gorilla/mux) via the command:

```bash
go mod tidy
```

To build the executable, and then install it on the bin directory:

```bash
go mod install
```

## Launch the service

From the root of the repository, you can launch the service via the following command:

```bash
cd pictures/ && go run .
```

## Request the api

Below, I describe how to interact with the endpoints of this service via a collection of curl.
See [here](https://curl.se/) for more information about curl.

Alternatively, [here](https://www.postman.com/letrome/workspace/farmhub/folder/7556688-6b67ca31-39ef-4887-8107-9719bf45b601?action=share&source=copy-link&creator=7556688&ctx=documentation)
is a postman collection with example of use of the api.

### List all pictures

```bash
curl -XGET localhost:5000/pictures
```

### Get a picture by id (example with 'picture of Chicken Little')

```bash
curl -XGET localhost:5000/pictures/picture6
```

### Create a picture (example with 'picture of Emile')

```bash
curl -v -XPOST localhost:5000/pictures -d'{
  "name":"picture of Emile",
  "type":"Farmer",
  "url":"/pictures/farmer/picture29.png"
}'
```

### Update a picture (example with picture2)

```bash
curl -v -XPUT localhost:5000/pictures/picture2 -d'{
  "name":"picture of Clarabella",
  "type":"Animal",
  "url":"/pictures/animals/picture2.png"
}'
```

### Update specific fields of a picture (update name for picture3)

```bash
curl -v -XPATCH localhost:5000/pictures/picture3 -d'{"name":"picture of Bebesse"}'
```

### Delete specific fields of a picture (picture4)

```bash
curl -v -XDELETE localhost:5000/pictures/picture4
```

### Reset data to the default values

```bash
curl -XPOST localhost:5000/reset
```

## Integration tests

Integration tests for this project are written in using postman (cf documentation). You can execute them directly from
postman (in this case, they are accessible
via [this link](https://www.postman.com/letrome/workspace/farmhub/folder/7556688-557eed33-8ed6-4565-af4f-ba79c3756d49?action=share&source=copy-link&creator=7556688&ctx=documentation)),
or directly from this project. In this case, you need to have newman installed. You can install it via the following
command (cf [documentation](https://learning.postman.com/docs/collections/using-newman-cli/installing-running-newman/)):

```bash
npm install -g newman
```

Once installed, launch the tests via the script `run.sh` in `pictures/integration-tests`:

```bash
pictures/integration-tests/run.sh
