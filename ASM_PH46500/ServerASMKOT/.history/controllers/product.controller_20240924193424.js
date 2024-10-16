const Product = require("../models/product")

exports.getProduct = async (req, res) => {
    try {
        const products = await Product.find();
        res.json(products);
    } catch (error) {
        res.status(500).json({ msg: 'Get product failed!' })
    }
};

exports.addProduct = async (req, res) => {
    const { name, image_url, price, description, category } = req.body;

    try {
        const product= new Product({
            name,
            image_url,
            price,
            description,
            category
        })

        await product.save();
        res.status(201).json({msg:'Add product successfully.'})
    } catch (error) {
        res.status(500).json({ msg: 'Add product failed!' });
    }
}