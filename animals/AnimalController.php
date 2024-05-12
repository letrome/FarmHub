<?php

use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;
use Slim\Factory\AppFactory;

require __DIR__ . '/vendor/autoload.php';
require __DIR__ . '/AnimalRepository.php';
require __DIR__ . '/AnimalService.php';
require __DIR__ . '/AnimalModel.php';

$app = AppFactory::create();

$app->addErrorMiddleware(true, true, true);


$animalRepository = new AnimalRepository();
$animalService = new AnimalService($animalRepository);

$app->get('/animals', function (Request $request, Response $response, $args) use ($animalService) {
    $animals = $animalService->getAllAnimals();
    $response->getBody()->write(json_encode($animals));
    return $response->withHeader('Content-Type', 'application/json')->withStatus(200);
});

$app->get('/animals/{id}', function (Request $request, Response $response, $args) use ($animalService) {
    $id = $args['id'];
    $animal = $animalService->getAnimalById($id);
    if ($animal) {
        $response->getBody()->write(json_encode($animal));
        return $response->withHeader('Content-Type', 'application/json')->withStatus(200);
    } else {
        $response->getBody()->write(json_encode(['error' => 'Animal not found']));
        return $response->withHeader('Content-Type', 'application/json')->withStatus(404);
    }
});

$app->post('/animals', function (Request $request, Response $response, $args) use ($animalService) {
    $body = json_decode($request->getBody(), true);
    $animal = new AnimalModel(
        name: $body['name'],
        birthDate: $body['birthDate'],
        specie: $body['specie'],
        owner: $body['owner'],
        status: $body['status'],
        picture: $body['picture']
    );

    $animal = $animalService->createAnimal($animal);

    $response->getBody()->write(json_encode($animal));
    return $response->withHeader('Content-Type', 'application/json')->withStatus(201);
});

$app->put('/animals/{id}', function (Request $request, Response $response, $args) use ($animalService) {
    $body = json_decode($request->getBody(), true);
    $id = $args['id'];
    $animal = new AnimalModel(
        name: $body['name'],
        birthDate: $body['birthDate'],
        specie: $body['specie'],
        owner: $body['owner'],
        status: $body['status'],
        picture: $body['picture']
    );

    $animal = $animalService->updateAnimal($id, $animal);

    if ($animal) {
        $response->getBody()->write(json_encode($animal));
        return $response->withHeader('Content-Type', 'application/json')->withStatus(200);
    } else {
        $response->getBody()->write(json_encode(['error' => 'Animal not found']));
        return $response->withHeader('Content-Type', 'application/json')->withStatus(404);
    }

});

$app->patch('/animals/{id}', function (Request $request, Response $response, $args) use ($animalService) {
    $updatedFields = json_decode($request->getBody(), true);
    $id = $args['id'];

    $animal = $animalService->patchAnimal($id, $updatedFields);

    if ($animal) {
        $response->getBody()->write(json_encode($animal));
        return $response->withHeader('Content-Type', 'application/json')->withStatus(200);
    } else {
        $response->getBody()->write(json_encode(['error' => 'Animal not found']));
        return $response->withHeader('Content-Type', 'application/json')->withStatus(404);
    }
});

$app->delete('/animals/{id}', function (Request $request, Response $response, $args) use ($animalService) {
    $id = $args['id'];

    $animal = $animalService->deleteAnimal($id);

    if ($animal) {
        $response->getBody()->write(json_encode($animal));
        return $response->withHeader('Content-Type', 'application/json')->withStatus(200);
    } else {
        $response->getBody()->write(json_encode(['error' => 'Animal not found']));
        return $response->withHeader('Content-Type', 'application/json')->withStatus(404);
    }
});

$app->post('/reset', function (Request $request, Response $response, $args) use ($animalService) {
    $animalService->reset();

    $response->getBody()->write(json_encode(['applied' => true]));
    return $response->withHeader('Content-Type', 'application/json')->withStatus(200);
});