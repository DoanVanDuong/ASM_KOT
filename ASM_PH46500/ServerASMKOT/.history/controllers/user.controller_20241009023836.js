const User = require("../models/user");
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");

//Register
exports.register = async (req, res) => {
    const { name, userName, email, password } = req.body;

    try {
        // check already email, username
        let user = await User.findOne({ $or: [{ email }, { userName }] });
        if (user) {
            return res.status(400).json({ msg: 'Already email or userName!' })
        }

        //mã hóa mk 
        const salt = await bcrypt.genSalt(10);
        const hashedPassword = await bcrypt.hash(password, salt);

        //create new user
        user = new User({
            name,
            userName,
            email,
            password: hashedPassword
        });

        await user.save();
        res.status(201).json({ msg: 'Register successfully.' })

    } catch (error) {
        res.status(500).json({ msg: 'Server error!' })
        console.log(error);
    }
};


// Login
exports.login = async (req, res) => {
    const { email, password } = req.body;

    try {
        //find user by email
        const user = await User.findOne({ email });
        if (!user) {
            return res.status(400).json({ msg: 'Email not found!' });
        }

        // check password
        const isMatchPass = await bcrypt.compare(password, user.password);
        if (!isMatchPass) {
            return res.status(400).json({ msg: 'Wrong passwors!' })
        }

        // create token
        const token = jwt.sign(
            { userId: user._id, userName: user.userName },
            process.env.JWT_SECRET,
            { expiresIn: '1h' }
        );

        const decoded =jwt.verify(token,process.env.JWT_SECRET);

        console.log(decoded);

        res.json({ token })

    } catch (error) {
        res.status(500).json({ msg: 'Server error!' })
        console.log(error);
    }
}; 


//get user info
// Get User Info
exports.getUserInfo = async (req, res) => {
    try {
        const userId = req.userId;

        const user = await User.findById(userId).select('-password');
        if (!user) {
            return res.status(404).json({ msg: 'User not found!' });
        }

        res.json(user);
    } catch (error) {
        res.status(500).json({ msg: 'Server error!' });
        console.log(error);
    }
};
