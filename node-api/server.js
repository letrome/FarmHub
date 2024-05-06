const express = require('express');
const app = express();
const port = 3000;

app.get('/', (req, res) => {
    res.send('Base url of the node api');
});

app.get('/api/data', (req, res) => {
    const data = { message: 'data data' };
    res.json(data);
});

app.listen(port, () => {
    console.log(`Node (using express) app running on port ${port}`);
});