var express = require('express');
var router = express.Router();
const p = require('../controllers/product.controller')

router.get('/', p.getProduct);
router.post('/add', p.addProduct);

module.exports = router