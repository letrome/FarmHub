# Usage

## Prerequisites

You should have Python (3.12) installed on the instance you want to run this service. If this is not the case, I
recommend you to have a look at [the Python Docs](https://www.python.org/).

This service use FastAPI ([documentation](https://fastapi.tiangolo.com/)). You can install-it (and also the other dependencies used in this service), in running the following command from the `farmer` folder:
```bash
pip install -r requirements.txt
```

## Launch the service

From the `farmer` folder, you can launch the service via the following command:
```bash
fastapi run main.py --host=192.168.1.4 --port=6000
```

## Request the api

Below, I describe how to interact with the endpoints of this service via a collection of curl.
See [here](https://curl.se/) for more information about curl.

Alternatively, [here](https://www.postman.com/letrome/workspace/farmhub/folder/7556688-d8de3882-207e-45ff-9ea7-1fcf8f9f2b6f?action=share&source=copy-link&creator=7556688&ctx=documentation)
is a postman collection with example of use of the api.

### List all farmers

```bash
curl -XGET localhost:6000/farmers
```

### Get a farmer by id (example with Alice)

```bash
curl -XGET localhost:6000/farmers/farmer1
```

### Create a farmer (example with José)

```bash
curl -XPOST localhost:6000/farmers -d'{
        "first_name":"José",
        "last_name":"Bové",
        "birth_date":"1953-06-11",
        "specialties":[
            "Cow",
            "Sheep",
            "Pig"
        ],
        "picture":"picture30",
        "status":"present"
    }'
    -H "Content-Type: application/json"
```

*suppose that we get the id 'farmer5' in answer of the farmer creation.*

### Update a farmer (replace José data with Stéphane data)

```bash
curl -XPUT localhost:6000/farmers/farmer5 -d'{
        "first_name": "Stéphane",
        "last_name": "Le Foll",
        "birth_date": "1960-02-03",
        "specialties": [
            "Cow",
            "Sheep",
            "Pig"
        ],
        "picture": "picture31",
        "status": "present"
    }'
    -H "Content-Type: application/json"
```

### Update specific fields of a farmer (update status for Stéphane)

```bash
curl -XPATCH localhost:6000/farmers/farmer5 -d'{
        "status": "gone"
    }'
    -H "Content-Type: application/json"
```

### Delete a farmer (delete Stéphane data)

```bash
curl -XDELETE localhost:6000/farmers/farmer5
```

### Reset data to the default values

```bash
curl -XPOST localhost:6000/reset
```

## Integration tests

Integration tests for this project are written in using postman (cf documentation). You can execute them directly from
postman (in this case, they are accessible
via [this link](https://www.postman.com/letrome/workspace/farmhub/folder/7556688-215eb1e6-1f04-4973-9fae-b13c466805af?action=share&source=copy-link&creator=7556688&ctx=documentation)),
or directly from this project.
In this case, you need to have newman installed. You can install it via the following command (
cf [documentation](https://learning.postman.com/docs/collections/using-newman-cli/installing-running-newman/)):

```bash
npm install -g newman
```

Once installed, from the `farmers` folder, launch the tests via the script `run.sh` in `integration-tests`:

```bash
integration-tests/run.sh
```