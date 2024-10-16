const Cart = require("../models/cart");
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
        const product = new Product({
            name,
            image_url,
            price,
            description,
            category
        })

        await product.save();
        res.status(201).json({ msg: 'Add product successfully.' })
    } catch (error) {
        res.status(500).json({ msg: 'Add product failed!' });
    }
}


//add to cart
exports.addToCart = async (req, res) => {
    const { productId, quantity } = req.body;
    const userId = req.userId;

    try {
        let cart = await Cart.findOne({ user: userId });

        if (cart) {
            const itemIndex = cart.items.findIndex(item => item.product.toString() === productId);
            if (itemIndex > 1) {
                cart.items[itemIndex].quantity += quantity;
            } else {
                cart.items.push({ produc: productId, quantity });
            }
        } else {
            cart = new Cart({
                user: userId,
                items: [{
                    product: productId, quantity
                }]
            })
        }

        await cart.save();
        res.status(201).json({ msg: 'Add to cart successfully.' })

    } catch (error) {
        console.log(error);
        res.status(500).json({ msg: 'Server error!' });
    }
};


// get cart
exports.getCart = async (req, res) => {
    const userId = req.userId

    try {
        const cart = await Cart.findOne({ user: userId }).populate('items.product');

        if (!cart) {
            return res.status(404).json({ msg: 'Empty cart' })
        }
        res.json(cart.items);

    } catch (error) {
        console.log(error);
        res.status(500).json({ msg: 'Server error!' });
    }
};