const Cart = require("../models/cart");
const Order = require("../models/order");
const Product = require("../models/product")

//get product
exports.getProduct = async (req, res) => {
    try {
        const products = await Product.find();
        res.json(products);
    } catch (error) {
        res.status(500).json({ msg: 'Get product failed!' })
    }
};

//Add product
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

//checkout
exports.checkout = async (req, res) => {
    const userId = req.userId;
    const { address, phoneNumber } = req.body;

    try {
        //get cart of user
        const cart = await Cart.findOne({ user: userId }).populate('items.product');
        if (!cart || cart.items.length === 0) {
            return res.status(400).json({msg:'Your cart is empty'})
        }

        //caculate total
        let totalAmount=0;
        cart.items.forEach(item => {
            totalAmount+=item.product.price*item.quantity;
        });

        //create new order
        const order= new Order({
            user:userId,
            items:cart.items.map(item=>({
                product:item.product._id,
                quantity:item.quantity
            })),
            totalAmount,
            shippingAddress:{
                address,phoneNumber
            },
        });

        //save order
        await order.save();

        //delete cart affter checkout
        await Cart.findOneAndDelete({user:userId});

        res.status(201).json({msg:'Your order was send successfully',order});

    } catch (error) {
        console.log(error);
        res.status(500).json({ msg: 'Server error!' });
    }
}
