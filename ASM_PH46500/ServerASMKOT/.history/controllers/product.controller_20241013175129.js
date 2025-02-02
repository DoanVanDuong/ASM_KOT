const Cart = require("../models/cart");
const Category = require("../models/category");
const Order = require("../models/order");
const Product = require("../models/product")

//get product
exports.getProduct = async (req, res) => {
    try {
        const products = await Product.find().populate('category', 'name');
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
        const product = await Product.findById(productId).populate('category', 'name');

        if (!product) {
            return res.status(404).json({ msg: "Product not found!" })
        }

        res.json(product)
    } catch (error) {
        console.log(error);
        res.status(500).json({ msg: 'Server error!' });
    }
}


// ===============================
//Category
// Add category
exports.addCategory = async (req, res) => {
    const { name } = req.body;

    if (!name) {
        return res.status(400).json({ msg: 'Category name is required' });
    }

    try {
        const category = new Category({ name });

        await category.save();
        res.status(201).json({ msg: 'Category added successfully', category });
    } catch (error) {
        console.error(error);
        res.status(500).json({ msg: 'Server error while adding category' });
    }
};

//get cate
exports.getCategory = async (req, res) => {
    try {
        const categories = await Category.find();
        res.json(categories);
    } catch (error) {
        res.status(500).json({ msg: 'Get Category failed!' })
    }
};

// ===============================

//add to cart
exports.addToCart = async (req, res) => {
    const { productId, quantity } = req.body;
    const userId = req.userId;
 
    try {
        let cart = await Cart.findOne({ user: userId });

        if (cart) {
            const itemIndex = cart.items.findIndex(item => item.product && item.product.toString() === productId);
            if (itemIndex > -1) {
                cart.items[itemIndex].quantity += quantity;
            } else {
                cart.items.push({ product: productId, quantity });
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

// Xóa hết các item trong giỏ hàng
exports.clearCart = async (req, res) => {
    const userId = req.userId;  
    try {
        await Cart.findOneAndDelete({ user: userId });
        res.status(200).json({ msg: 'Giỏ hàng đã được xóa thành công.' });
    } catch (error) {
        console.log(error);
        res.status(500).json({ msg: 'Lỗi máy chủ!' });
    }
};


//checkout
exports.checkout = async (req, res) => {
    const userId = req.userId;
    const { address, phoneNumber,paymentMethod } = req.body;

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
            paymentMethod:paymentMethod,
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
