const mongoose = require('mongoose');
require('dotenv').config();
const DB_NAME = process.env.DB_NAME;

mongoose.connect('mongodb://127.0.0.1:27017/' + DB_NAME)
    .catch((err) => {
        console.log('connect database failed');
        console.log(err);
    })

module.exports = { mongoose }
