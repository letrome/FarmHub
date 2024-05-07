const express = require('express');
const bodyParser = require('body-parser');
const controller = require('./controller');

const app = express();

app.use(bodyParser.json());

controller.setupRoutes(app);

const port = process.argv[2] || 3000;

app.listen(port, () => {
  console.log(`Server running on port ${port}`);
});