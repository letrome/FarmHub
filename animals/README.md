# Usage

## Prerequisites

You should have php (8.3) installed on the instance you want to run this service. If this is not the case, I
recommend you to have a look at [the php Docs](https://www.php.net/manual/en/install.php).

This service also use the framework slim. To be able to install it, you first need to install composer. I recommend you to have a look on [the documentation](https://getcomposer.org/doc/00-intro.md). To keep it short, you can install it as follows, from the folder `animal`:
```bash
php -r "copy('https://getcomposer.org/installer', 'composer-setup.php');"\n
php -r "if (hash_file('sha384', 'composer-setup.php') === 'dac665fdc30fdd8ec78b38b9800061b4150413ff2e3b6f88543c636f7cd84f6db9189d43a81e5503cda447da73c7e5b6') { echo 'Installer verified'; } else { echo 'Installer corrupt'; unlink('composer-setup.php'); } echo PHP_EOL;"\n
php composer-setup.php
php -r "unlink('composer-setup.php');"
mv composer.phar composer
chmod +x composer
```

Then, create a file 'composer.json' with the following content:
```json
{
    "require": {
        "slim/slim": "4.*",
        "slim/psr7": "^1.5"
    }
}
```

Finally, install slim in running the following command:
```bash
./composer install
```

## Launch the service

From the root of the repository, you can launch the service via the following command:

```bash
php -S localhost:4000 -t animals/public
 > [/* current date and time */] PHP 8.3.6 Development Server (http://localhost:4000) started
```

## Request the api

Below, I describe how to interact with the endpoints of this service via a collection of curl.
See [here](https://curl.se/) for more information about curl.

Alternatively, [here](https://www.postman.com/letrome/workspace/farmhub/folder/7556688-1dc93549-bf97-49d2-abc6-8298ec18340b?action=share&source=copy-link&creator=7556688&ctx=documentation)
is a postman collection with example of use of the api.

### List all animals

```bash
curl -XGET http://localhost:4000/animals
```

### Get an animal by id (example with Marguerite)

```bash
curl -XGET http://localhost:4000/animals/animal1
```

### Create an animal (example with Poulain)

```bash
curl -XPOST http://localhost:4000/animals -d '{
        "name": "Poulain",
        "birth_date": "1848-02-01",
        "specie": "specie4",
        "farmer": "farmer1",
        "status": "gone",
        "picture": "picture32"
    }'
    -H "Content-Type: application/json"
```

*suppose that we get the id 'animal25' in answer of the animal creation.*

### Update an animal (replace Poulain data with Rapidash data)

```bash
curl -XPUT http://localhost:4000/animals/animal25 -d '{
        "name": "Rapidash",
        "birth_date": "1996-02-27",
        "specie": "specie4",
        "farmer":"farmer2",
        "status":"present",
        "picture":"picture33"
    }'
    -H "Content-Type: application/json"
```

### Update specific fields of an animal (update status for Rapidash)

```bash
curl -XPATCH http://localhost:4000/animals/animal25 -d '{
        "status": "gone"
    }' 
    -H "Content-Type: application/json"
```

### Delete an animal (delete Rapidash data)

```bash
curl -XDELETE http://localhost:4000/animals/animal25
```

### Reset data to the default values

```bash
curl -XPOST http://localhost:4000/reset
```

## Integration tests

Integration tests for this project are written in using postman (cf documentation). You can execute them directly from
postman (in this case, they are accessible
via [this link](https://www.postman.com/letrome/workspace/farmhub/folder/7556688-b68b7fa0-ca36-4282-a1a7-ce6b20238ad0?action=share&source=copy-link&creator=7556688&ctx=documentation)),
or directly from this project.
In this case, you need to have newman installed. You can install it via the following command (
cf [documentation](https://learning.postman.com/docs/collections/using-newman-cli/installing-running-newman/)):

```bash
npm install -g newman
```

Once installed, launch the tests via the script `run.sh` in `animals/integration-tests`:

```bash
animals/integration-tests/run.sh
```