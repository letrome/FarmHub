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
curl -XGET http://localhost:3000/species
```

### Get a specie by id (example with the cow)

```bash
curl -XGET http://localhost:3000/species/specie1
```

### Create a new specie (example with the goose)

```bash
curl -XPOST http://localhost:3000/species -d '{
        "name": "Goose",
        "description": "The goose is a waterfowl bird known for its honking sound.",
        "diet": "Herbivore",
        "naturalHabitat": "Wetlands",
        "conservationStatus": "Least Concern"
    }'
    -H "Content-Type: application/json"
```

*suppose that we get the id 'specie7' in answer of the specie creation.*

### Update a specie (replace goose data with goat data)

```bash
curl -XPUT localhost:3000/species/specie7 -d '{
        "name": "Goat",
        "description": "The goat is a domesticated herbivorous mammal.",
        "diet": "Herbivore",
        "naturalHabitat": "Grasslands",
        "conservationStatus": "Least Concern"
    }'
    -H 'Content-type: application/json'
```

### Update specific fields of a specie (update description for goat)

```bash
curl -XPATCH localhost:3000/species/specie7 -d'{
        "description": "The goat is a domesticated herbivorous mammal with horns."
    }'
    -H 'Content-type: application/json'
```

### Delete a specie (delete horse data)

```bash
curl -XDELETE http://localhost:3000/species/specie7
```

### Reset data to the default values

```bash
curl -XPOST http://localhost:3000/reset
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