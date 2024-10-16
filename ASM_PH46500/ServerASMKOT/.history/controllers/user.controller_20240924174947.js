const User = require("../models/user");
const bcrypt = require("bcryptjs");

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
        const hashedPassword = await bcrypt.hash(password,salt);

        //create new user
        user = new User({
            name,
            userName,
            email,
            password:hashedPassword
        });

        await user.save();
        res.status(201).json({msg:'Register successfully.'})

    } catch (error) {
        res.status(500).json({ msg: 'Server error!' })
        console.log(err);
    }
}