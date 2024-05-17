const Species = require('./model');
const fs = require('fs');

const dbFile = "resources/db.txt";

let speciesData = [];
let lastAssignedId;

async function initData() {
  if (speciesData.length === 0) {
    return new Promise((resolve, reject) => {
      fs.readFile(dbFile, 'utf8', (err, data) => {
        if (err) {
          console.error(err);
        } else {
          if (data.length > 0) {
            const parsedData = JSON.parse(data);
            lastAssignedId = parsedData['lastAssignedId'];
            speciesData = parsedData['speciesData'];

            console.log('Data loaded');
          }

          if (speciesData.length === 0) {
            loadDefaultData();
            console.log('Default data loaded');
          }

          resolve();
        }
      });
    });
  }
}

async function saveData() {
  return new Promise((resolve, reject) => {

    const data = JSON.stringify({speciesData, lastAssignedId});

    fs.writeFile(dbFile, data, 'utf8', (err) => {
      if (err) {
        console.error(err);
        return;
      }
      console.log('Data saved');
    });
    resolve();
  });
}

function loadDefaultData() {
  speciesData = [
    new Species('specie1', 'Cow', 'The cow is a domesticated herbivorous mammal.', 'Herbivore', 'Grasslands', 'Least Concern'),
    new Species('specie2', 'Chicken', 'The chicken is a domesticated bird.', 'Omnivore', 'Farmland', 'Least Concern'),
    new Species('specie3', 'Sheep', 'The sheep is a domesticated herbivorous mammal.', 'Herbivore', 'Grasslands', 'Least Concern'),
    new Species('specie4', 'Horse', 'The horse is a large herbivorous mammal.', 'Herbivore', 'Grasslands', 'Least Concern'),
    new Species('specie5', 'Pig', 'The pig is a domesticated omnivorous mammal.', 'Omnivore', 'Farmland', 'Least Concern'),
    new Species('specie6', 'Duck', 'The duck is a waterfowl bird.', 'Omnivore', 'Wetlands', 'Least Concern')
  ];

  lastAssignedId = 6;
}

async function getAllSpecies() {
  await initData();
  return speciesData;
}

async function getSpecieById(id) {
  await initData();
  return speciesData.find(species => species.id === id);
}

async function createSpecie(species) {
  await initData();

  const newId = "specie" + (lastAssignedId + 1).toString();
  const newSpecie = new Species(newId, species.name, species.description, species.diet, species.natural_habitat, species.conservation_status);
  speciesData.push(newSpecie);
  lastAssignedId++;

  await saveData();
  return newSpecie;
}

async function updateSpecie(id, updatedSpecie) {
  await initData();

  const index = speciesData.findIndex(species => species.id === id);
  if (index !== -1) {
    updatedSpecie.id = id;
    speciesData[index] = updatedSpecie;

    await saveData();
    return updatedSpecie;
  }
  return null;
}

async function patchSpecie(id, updatedFields) {
  await initData();

  const index = speciesData.findIndex(species => species.id === id);
  if (index !== -1) {
    const originalSpecies = speciesData[index];
    const updatedSpecies = {
      id: originalSpecies.id,
      name: updatedFields.name || originalSpecies.name,
      description: updatedFields.description || originalSpecies.description,
      diet: updatedFields.diet || originalSpecies.diet,
      natural_habitat: updatedFields.natural_habitat || originalSpecies.natural_habitat,
      conservation_status: updatedFields.conservation_status || originalSpecies.conservation_status
    };
    speciesData[index] = new Species(
      updatedSpecies.id,
      updatedSpecies.name,
      updatedSpecies.description,
      updatedSpecies.diet,
      updatedSpecies.natural_habitat,
      updatedSpecies.conservation_status
    );

    await saveData();
    return speciesData[index];
  }
  return null;
}

async function deleteSpecie(id) {
  await initData();

  const index = speciesData.findIndex(species => species.id === id);
  if (index !== -1) {
    const deletedSpecies = speciesData.splice(index, 1);

    await saveData();
    return deletedSpecies[0];
  }
  return null;
}

async function reset() {
  await loadDefaultData();
  await saveData();
}

module.exports = {
  getAllSpecies,
  getSpecieById,
  createSpecie,
  updateSpecie,
  patchSpecie,
  deleteSpecie,
  reset
};