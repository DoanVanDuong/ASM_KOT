var express = require('express');
var router = express.Router();
const uc = require('../controllers/user.controller')
const authMiddleware = require('../middlewares/authMiddleware')

router.post('/register',uc.register)
router.post('/login',uc.login)

router.get('/getUserInfo',authMiddleware,uc.getUserInfo)

module.exports = router;
