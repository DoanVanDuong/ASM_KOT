var express = require('express');
var router = express.Router();
const uc = require('../controllers/user.controller')

router.post('/register',uc.register)
router.post('/login',uc.login)

router.get('/getUserInfo',uc.getUserInfo)

module.exports = router;
