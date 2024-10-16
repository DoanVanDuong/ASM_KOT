const jwt = require('jsonwebtoken');

const authMiddleqare = (req, res, next) => {
    const token = req.header('Authorization');
    if (!token) {
        return res.status(401).json({ msg: 'Token not found, deny access!' })
    }

    try {
        const decoded = jwt.verify(token.split(' ')[1], process.env.JWT_SECRET);
        req.userId = decoded.userId;
        next();
    } catch (error) {
        console.log(error);
        res.status(401).json({ msg: 'invalid token!' })
    }
}

module.exports = authMiddleqare