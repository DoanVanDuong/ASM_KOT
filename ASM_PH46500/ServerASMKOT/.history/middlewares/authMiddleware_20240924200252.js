const jwt = require('jsonwebtoken');

const authMiddleqare = (req, res, next) => {
    const token = req.header('Authorization');
    if (!token) {
        return res.status(401).json({ msg: 'Token not found, deny access!' })
    }

    try {

    } catch (error) {
        console.log(error);
        res.status(401).json({ msg: 'invalid token!' })
    }
}

module.exports = authMiddleqare