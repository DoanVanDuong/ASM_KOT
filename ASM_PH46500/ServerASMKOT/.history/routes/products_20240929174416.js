var express = require('express');
var router = express.Router();
const p = require('../controllers/product.controller')
const authMiddleware = require('../middlewares/authMiddleware')

router.get('/', p.getProduct);
router.post('/addProduct', p.addProduct);

router.post('/addToCart', authMiddleware, p.addToCart);
router.get('/getCart', authMiddleware, p.getCart);

router.post('/checkout', authMiddleware, p.checkout);


module.exports = router