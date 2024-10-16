var express = require('express');
var router = express.Router();

router.post('/reg',uc.reg)
router.post('/login',uc.login)

module.exports = router;
