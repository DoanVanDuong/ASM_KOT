var express = require('express');
var router = express.Router();
const p = require('../controllers/product.controller')
const authMiddleware = require('../middlewares/authMiddleware')

router.get('/', p.getProduct);
router.post('/addProduct', p.addProduct);
router.put('/editProduct/:productId', p.editProduct);
router.delete('/deleteProduct/:productId', p.deleteProduct);

router.post('/addToCart', authMiddleware, p.addToCart);
router.get('/getCart', authMiddleware, p.getCart);

router.post('/checkout', authMiddleware, p.checkout);
router.get('/getOrder', authMiddleware, p.getOrder);

router.get('/search', p.search);


module.exports = router