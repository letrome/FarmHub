# Usage

## Prerequisites

You should have Java (21) installed on the instance you want to run this service. If this is not the case, I
recommend you to have a look at [the Oracle.com website](https://www.oracle.com/fr/java/technologies/downloads/#java21).

This service also use Maven as dependcy management solution. More information on how to install it
on [the Maven website](https://maven.apache.org).

## Download dependencies and build the service

From the folder farm, you can download the dependencies and build the service in running the following command:

```bash
mvn compile
```

## Launch the service

From the folder farm, you can launch the service via the following command:

```bash
mvn spring-boot:run
```

By default, it will use the configuration file "resources/application.properties". You can specify another profile, in
setting up an environment variable as follows:

```bash
export SPRING_PROFILES_ACTIVE=THE_PROFILE
mvn spring-boot:run
```

With `THE_PROFILE` = the name of the profile you want to use. Two profiles are available in this repository: `local`, to launch the service in using the other services (species,
animals, pictures, farmers) that are running locally, and `intg` to execute the service's integration tests. If you want
to use the default profile, you have to specify the host of the different services.

## Troobleshooting

In case where you encounter an error similar to:

```
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.8.0:compile (default-compile) on project farm: Fatal error compiling: invalid flag: --release -> [Help 1]
```

It implies that your environment variable JAVA_HOME is not pointing to the right installation of java. You can solve it
in setting this variable to the right installation folder of Java.

On macOS, this can be achieved as follows. First list the different version of java installed via the command:
```bash
/usr/libexec/java_home -V
```

In my case, I get the following answer:
```bash
Matching Java Virtual Machines (4):
    22.0.1 (arm64) "Oracle Corporation" - "OpenJDK 22.0.1" /Users/romain.letourneur/Library/Java/JavaVirtualMachines/openjdk-22.0.1/Contents/Home
    21.0.3 (arm64) "Amazon.com Inc." - "Amazon Corretto 21" /Users/romain.letourneur/Library/Java/JavaVirtualMachines/corretto-21.0.3/Contents/Home
    19.0.2 (arm64) "Amazon.com Inc." - "Amazon Corretto 19" /Users/romain.letourneur/Library/Java/JavaVirtualMachines/corretto-19.0.2/Contents/Home
    17.0.11 (arm64) "Homebrew" - "OpenJDK 17.0.11" /Library/Java/JavaVirtualMachines/openjdk-17.jdk/Contents/Home
```

Then, since in our case, we are interested in the version 21.X.X, assign the right path to the variable JAVA_HOME in executing the command:
```bash
export JAVA_HOME=`/usr/libexec/java_home -v 21.0.3`
```

Finally, to avoid to have to set JAVA_HOME again, simply add the 2 previous commands to your file `~/.bash_profile`.

## Request the api
Below, I describe how to interact with the endpoints of this service via a collection of curl.
See [here](https://curl.se/) for more information about curl.

Alternatively, [here](https://www.postman.com/letrome/workspace/farmhub/folder/7556688-79267a6c-d54d-419f-bac6-722d3e9e0764?action=share&source=copy-link&creator=7556688&ctx=documentation)
is a postman collection with example of use of the api.

Note that there are several subroutes on this service: one for farm, and one per service.

### Farm

#### Request the farm

```bash
curl -XGET localhost:7000/farm
```

#### Update specific fields of the farm (update farm_name)

```bash
curl -XPATCH localhost:7000/farm -d'{
        "farm_name": "Farming Simulator"
    }'
    -H "Content-type: application/json"
```

#### Update the farm (update farm_name and beginning_date)

```bash
curl -XPUT localhost:7000/farm -d'{
        "farm_name": "Stardew Valley",
        "beginning_date": "2016-02-26"
    }'
    -H "Content-type: application/json"
```

#### Reset farm, species, animals, pictures and farmers to their default values

```bash
curl -XPOST localhost:7000/farm/reset
```

### Species

#### List all species

```bash
curl -XGET localhost:7000/species
```

#### Get a specie by id (example with the cow)

```bash
curl -XGET localhost:7000/species/specie1
```

#### Create a new specie (example with the goose)

```bash
curl -XPOST localhost:7000/species -d '{
        "name": "Goose",
        "description": "The goose is a waterfowl bird known for its honking sound.",
        "diet": "Herbivore",
        "naturalHabitat": "Wetlands",
        "conservationStatus": "Least Concern"
    }'
    -H 'Content-type: application/json'
```

*suppose that we get the id 'specie7' in answer of the specie creation.*

#### Update a specie (replace goose data with goat data)

```bash
curl -XPUT localhost:7000/species/specie7 -d '{
        "name": "Goat",
        "description": "The goat is a domesticated herbivorous mammal.",
        "diet": "Herbivore",
        "naturalHabitat": "Grasslands",
        "conservationStatus": "Least Concern"
    }'
    -H 'Content-type: application/json'
```

#### Update specific fields of a specie (update description for goat)

```bash
curl -XPATCH localhost:7000/species/specie7 -d'{
        "description": "The goat is a domesticated herbivorous mammal with horns."
    }'
    -H 'Content-type: application/json'
```

#### Delete a specie (delete goat data)

```bash
curl -XDELETE localhost:7000/species/specie7
    -H 'Content-type: application/json'
```

### Pictures

#### List all pictures

```bash
curl -XGET localhost:7000/pictures
```

#### Get a picture by id (example with 'picture of Chicken Little')

```bash
curl -XGET localhost:7000/pictures/picture6
```

#### Create a picture (example with 'picture of Jacky')

```bash
curl -XPOST localhost:7000/pictures -d'{
        "name": "picture of Jacky",
        "type": "Farmer",
        "url": "/pictures/farmer/picture29.png"
    }'
    -H "Content-Type: application/json"
```

*suppose that we get the id 'picture29' in answer of the picture creation.*

#### Update a picture (example with picture29)

```bash
curl -XPUT localhost:7000/pictures/picture29 -d'{
        "name": "picture of Emile",
        "type": "Farmer",
        "url": "/pictures/farmer/picture29.png"
    }'
    -H "Content-Type: application/json"
```

#### Update specific fields of a picture (update name for picture29)

```bash
curl -XPATCH localhost:7000/pictures/picture29 -d'{
        "name": "Emile, \"the killer\""
    }'
    -H "Content-Type: application/json"
```

#### Delete a picture (picture29)

```bash
curl -XDELETE localhost:7000/pictures/picture29
```

### Farmers

#### List all farmers

```bash
curl -XGET localhost:7000/farmers
```

#### Get a farmer by id (example with Alice)

```bash
curl -XGET localhost:7000/farmers/farmer1
```

#### Create a farmer (example with José)

##### Step 1. Create the picture

```bash
curl -XPOST localhost:7000/pictures -d'{
        "name": "picture of José",
        "type": "Farmer",
        "url": "/pictures/farmers/picture30.png"
    }'
    -H "Content-Type: application/json"
```

*suppose that we get the id 'picture30' in answer of the picture creation.*

##### Step 2. Create the farmer

```bash
curl -XPOST localhost:7000/farmers -d'{
        "first_name":"José",
        "last_name":"Bové",
        "birth_date":"1953-06-11",
        "specialties":[
            "Cow",
            "Sheep",
            "Pig"
        ],
        "picture":"picture30",
        "status":"Active"
    }'
    -H 'Content-type: application/json'
```

*suppose that we get the id 'farmer5' in answer of the farmer creation.*

#### Update a farmer (replace José data with Stéphane data)

##### Step 1. Create the picture

```bash
curl -XPOST localhost:7000/pictures -d'{
        "name": "picture of Stéphane",
        "type": "Farmer",
        "url": "/pictures/farmers/picture31.png"
    }'
    -H "Content-Type: application/json"
```

*suppose that we get the id 'picture31' in answer of the picture creation.*

##### Step 2. Update the farmer with the new data

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

#### Update specific fields of a farmer (update status for Stéphane)

```bash
curl -XPATCH localhost:7000/farmers/farmer5 -d'{
        "status": "gone"
    }'
    -H 'Content-type: application/json'
```

#### Delete a farmer (delete Stéphane data)

```bash
curl -XDELETE localhost:7000/farmers/farmer5
```

### Animals

#### List all animals

```bash
curl -XGET http://localhost:7000/animals
```

#### Get an animal by id (example with Marguerite)

```bash
curl -XGET localhost:7000/animals/animal1
```

#### Create an animal (example with Poulain)

##### Step 1. Create the picture

```bash
curl -XPOST localhost:7000/pictures -d'{
        "name": "picture of Poulain",
        "type": "Animal",
        "url": "/pictures/animals/picture32.png"
    }'
    -H "Content-Type: application/json"
```

*suppose that we get the id 'picture32' in answer of the picture creation.*

##### Step 2. Create the animal

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

#### Update an animal (replace Poulain data with Rapidash data)

##### Step 1. Create the picture

```bash
curl -XPOST localhost:7000/pictures -d'{
        "name": "picture of Rapidash",
        "type": "Animal",
        "url": "/pictures/animals/picture33.png"
    }'
    -H "Content-Type: application/json"
```

*suppose that we get the id 'picture33' in answer of the picture creation.*

##### Step 2. Update the animal with the new data

```bash
curl -XPUT localhost:7000/animals/animal25 -d'{
        "name": "Rapidash",
        "birth_date": "1996-02-27",
        "specie": "specie4",
        "farmer":"farmer2",
        "status":"present",
        "picture":"picture33"
    }'
    -H "Content-type: application/json"
```

#### Update specific fields of an animal (update status for Rapidash)

```bash
curl -XPATCH localhost:7000/animals/animal25 -d'{
        "status": "gone"
    }'
    -H "Content-type: application/json" 
```

#### Delete an animal (delete Rapidash data)

```bash
curl -XDELETE localhost:7000/animals/animal25
    -H "Content-type: application/json"
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

Once installed, launch the tests via the script `run.sh` in `farm/integration-tests`:

```bash
farm/integration-tests/run.sh
```