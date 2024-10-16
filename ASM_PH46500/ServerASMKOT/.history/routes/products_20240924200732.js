var express = require('express');
var router = express.Router();
const p = require('../controllers/product.controller')
const authMiddleware = require('../middlewares/authMiddleware')

router.get('/', p.getProduct);
router.post('/addProduct', p.addProduct);

router.post('/addToCart', authMiddleware,p.addToCart);

module.exports = router