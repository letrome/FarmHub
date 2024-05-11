const service = require('./service');

async function getAllSpecies(req, res) {
  const species = await service.getAllSpecies();
  res.json(species);
}

async function getSpecieById(req, res) {
  const id = req.params.id;
  const species = await service.getSpecieById(id);
  if (species) {
    res.json(species);
  } else {
    res.status(404).json({ error: 'Specie not found' });
  }
}

async function createSpecie(req, res) {
  const specie = req.body;
  const newSpecie = await service.createSpecie(specie);
  res.status(201).json(newSpecie);
}

async function updateSpecie(req, res) {
  const id = req.params.id;
  const updatedSpecie = req.body;
  const specie = await service.updateSpecie(id, updatedSpecie);
  if (specie) {
    res.json(specie);
  } else {
    res.status(404).json({ error: 'Specie not found' });
  }
}

async function patchSpecie(req, res) {
  const id = req.params.id;
  const updatedFields = req.body;
  const specie = await service.patchSpecie(id, updatedFields);
  if (specie) {
    res.json(specie);
  } else {
    res.status(404).json({ error: 'Specie not found' });
  }
}

async function deleteSpecie(req, res) {
  const id = req.params.id;
  const deletedSpecie = await service.deleteSpecie(id);
  if (deletedSpecie) {
    res.json(deletedSpecie);
  } else {
    res.status(404).json({ error: 'Specie not found' });
  }
}

async function reset(req, res) {
  await service.reset();
  res.json({applied: true});
}

function setupRoutes(app) {
  app.get('/species', getAllSpecies);
  app.get('/species/:id', getSpecieById);
  app.post('/species', createSpecie);
  app.put('/species/:id', updateSpecie);
  app.patch('/species/:id', patchSpecie);
  app.delete('/species/:id', deleteSpecie);
  app.post('/reset', reset);
}

module.exports = {
  setupRoutes,
};