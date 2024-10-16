const Product = require("../models/product")

exports.getProduct = async (req, res) => {
    try {
        const products = await Product.find();
        res.json(products);
    } catch (error) {
        res.status(500).json({ msg: 'Get product failed!' })
    }
}