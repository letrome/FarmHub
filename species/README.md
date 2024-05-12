# Usage

## Prerequisites

You should have node and npm installed on the instance you want to run this service. If this is not the case, I
recommend you to have a look at [the npm Docs](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm/).

This service also use the framework Express.js. You can install it via:

```bash
npm install express
```

## Launch the service

From the root of the repository, you can launch the service via the following command:

```bash
node species/server.js
 > Server running on port 3000
```

By default, the server runs on port 3000. Alternatively, you can specify another port as follows (here, port = 1234):

```bash
node species/server.js 1234
 > Server running on port 1234
```

## Request the api

Below, I describe how to interact with the endpoints of this service via a collection of curl.
See [here](https://curl.se/) for more information about curl.

Alternatively, [here](https://www.postman.com/letrome/workspace/farmhub/folder/7556688-79267a6c-d54d-419f-bac6-722d3e9e0764?action=share&source=copy-link&creator=7556688&ctx=documentation)
is a postman collection with example of use of the api.

### List all species

```bash
curl -X GET http://localhost:3000/species
```

### Get a specie by id (example with the cow)

```bash
curl -X GET http://localhost:3000/species/specie1
```

### Create a new specie (example with the goose)

```bash
curl -X POST -H "Content-Type: application/json" -d '{
"name": "Goose",
"description": "The goose is a waterfowl bird known for its honking sound.",
"diet": "Herbivore",
"naturalHabitat": "Wetlands",
"conservationStatus": "Least Concern"
}' http://localhost:3000/species
```

### Update a specie (replace pig data with goat data)

```bash
curl -X PUT -H "Content-Type: application/json" -d '{
"name": "Goat",
"description": "The goat is a domesticated herbivorous mammal.",
"diet": "Herbivore",
"naturalHabitat": "Grasslands",
"conservationStatus": "Least Concern"
}' http://localhost:3000/species/specie5
```

### Update specific fields of a specie (update description for goat)

```bash
curl -X PATCH -H "Content-Type: application/json" -d '{
"description": "The goat is a domesticated herbivorous mammal with horns."
}' http://localhost:3000/species/specie5
```

### Delete a specie (delete horse data)

```bash
curl -X DELETE http://localhost:3000/species/specie4
```

### Reset data to the default values

```bash
curl -X POST http://localhost:3000/reset
```

## Integration tests

Integration tests for this project are written in using postman (cf documentation). You can execute them directly from
postman (in this case, they are accessible
via [this link](https://www.postman.com/letrome/workspace/farmhub/folder/7556688-a2b96fde-9c16-41cf-8976-4ce32c383e3f?action=share&source=copy-link&creator=7556688&ctx=documentation)),
or directly from this project.
In this case, you need to have newman installed. You can install it via the following command (
cf [documentation](https://learning.postman.com/docs/collections/using-newman-cli/installing-running-newman/)):

```bash
npm install -g newman
```

Once installed, launch the tests via the script `run.sh` in `species/integration-tests`:

```bash
species/integration-tests/run.sh
```