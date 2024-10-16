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
        res.status(201).json({ msg: 'Add product successfully.', product })
    } catch (error) {
        res.status(500).json({ msg: 'Add product failed!' });
    }
}

// Edit product
exports.editProduct = async (req, res) => {
    const { productId } = req.params; // Get productId from URL params
    const { name, image_url, price, description, category } = req.body; // Product data from request body

    try {
        const product = await Product.findById(productId);

        if (!product) {
            return res.status(404).json({ msg: 'Product not found!' });
        }

        // Update product fields
        product.name = name || product.name;
        product.image_url = image_url || product.image_url;
        product.price = price || product.price;
        product.description = description || product.description;
        product.category = category || product.category;

        // Save updated product
        await product.save();
        res.status(200).json({ msg: 'Product updated successfully.', product });

    } catch (error) {
        console.log(error);
        res.status(500).json({ msg: 'Server error!' });
    }
};


// Delete product
exports.deleteProduct = async (req, res) => {
    const { productId } = req.params; // Get productId from URL params

    try {
        const product = await Product.findByIdAndDelete(productId);

        if (!product) {
            return res.status(404).json({ msg: 'Product not found!' });
        }

        // Remove product from database
        // await product.remove();
        res.status(200).json({ msg: 'Product deleted successfully.' });

    } catch (error) {
        console.log(error);
        res.status(500).json({ msg: 'Server error!' });
    }
};

// get detail product
exports.getProductDetail = async (req, res) => {
    const { productId } = req.params;

    try {
        const product = await Product.findById(productId);

        if (!product) {
            return res.status(404).json({ msg: "Product not found!" })
        }

        res.json(product)
    } catch (error) {
        console.log(error);
        res.status(500).json({ msg: 'Server error!' });
    }
}


//add to cart
exports.addToCart = async (req, res) => {
    const { productId, quantity } = req.body;
    const userId = req.userId;

    try {
        if (!req.headers.authorization) {
            return res.status(401).json({ 
                success: false, 
                message: "Không tìm thấy token xác thực" 
            });
        }

        const token = req.headers.authorization.split(' ')[1];

        if (!token) {
            return res.status(401).json({ 
                success: false, 
                message: "Token không hợp lệ" 
            });
        }

        const decoded = jwt.verify(token, process.env.JWT_SECRET);
        const userId = decoded.userId;

        const { productId, quantity } = req.body;

        const product = await Product.findById(productId);
        if (!product) {
            return res.status(404).json({ 
                success: false, 
                message: "Không tìm thấy sản phẩm" 
            });
        }
        
        let cart = await Cart.findOne({ user: userId });

        if (cart) {
            const itemIndex = cart.items.findIndex(item => item.product.toString() === productId);
            if (itemIndex > -1) {
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
            return res.status(400).json({ msg: 'Your cart is empty' })
        }

        //caculate total
        let totalAmount = 0;
        cart.items.forEach(item => {
            totalAmount += item.product.price * item.quantity;
        });

        //create new order
        const order = new Order({
            user: userId,
            items: cart.items.map(item => ({
                product: item.product._id,
                quantity: item.quantity
            })),
            totalAmount,
            shippingAddress: {
                address, phoneNumber
            },
        });

        //save order
        await order.save();

        //delete cart affter checkout
        await Cart.findOneAndDelete({ user: userId });

        res.status(201).json({ msg: 'Your order was send successfully', order });

    } catch (error) {
        console.log(error);
        res.status(500).json({ msg: 'Server error!' });
    }
}

//get list ordered
exports.getOrder = async (req, res) => {
    const userId = req.userId;

    try {
        const orders = await Order.find({ user: userId }).populate('items.product').sort({ createdAt: -1 });

        if (!orders || orders.length === 0) {
            return res.status(404).json({ msg: 'your order is empty' });
        }
        res.json(orders);
    } catch (error) {
        console.log(error);
        res.status(500).json({ msg: 'Server error!' });
    }
}

//search by name
exports.search = async (req, res) => {
    const { q } = req.query;

    try {
        if (!q) {
            return res.status(400).json({ msg: 'Please enter search keyword!' })
        }
        const products = await Product.find({ name: { $regex: q, $options: 'i' } });

        if (products.length === 0) {
            return res.status(404).json({ msg: 'not found any item' })
        }

        res.json(products)

    } catch (error) {
        console.log(error);
        res.status(500).json({ msg: 'Server error!' });
    }

}
