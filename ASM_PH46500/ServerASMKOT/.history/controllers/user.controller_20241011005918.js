const User = require("../models/user");
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const multer = require('multer');
const path = require('path');
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

// Update user info
exports.updateUser = async (req, res) => {
    const { name, dateOfBirth, gender } = req.body;
    const userId = req.userId;  

    try {
        const user = await User.findById(userId);
        if (!user) {
            return res.status(404).json({ msg: 'User not found!' });
        }

        user.name = name || user.name; // Nếu không có giá trị mới, giữ nguyên giá trị cũ
        user.dateOfBirth = dateOfBirth || user.dateOfBirth;
        user.gender = gender || user.gender;

        await user.save();
        res.json({ msg: 'User info updated successfully', user });
    } catch (error) {
        res.status(500).json({ msg: 'Server error!' });
        console.log(error);
    }
};

////- Update avatar
// Cấu hình multer để lưu trữ ảnh
const storage = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, 'public/uploads/');
    },
    filename: function (req, file, cb) {
        cb(null, file.fieldname + '-' + Date.now() + path.extname(file.originalname));
    }
});


// Kiểm tra loại file (chỉ cho phép ảnh)
const fileFilter = (req, file, cb) => {
    const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png'];
    if (!allowedTypes.includes(file.mimetype)) {
        return cb(new Error('Only images are allowed'), false);
    }
    cb(null, true);
};

const upload = multer({
    storage: storage,
    fileFilter: fileFilter
}).single('avatar');

// api update avatar
exports.updateAvatar =async(req,res)=>{
    const userId = req.userId

    try {
        upload(req,res,async(err)=>{
            if (err) {
                return res.status(400).json({ msg:'Error uploading file: '+ err.message });
            }
             // Kiểm tra nếu không có file
             if (!req.file) {
                return res.status(400).json({ msg: 'No file uploaded!' });
            }
             // Lấy URL của ảnh đã tải lên (giả sử server host file tại /uploads)
             const avatarUrl = `/uploads/${req.file.filename}`;
             // Cập nhật avatar cho user
            const user = await User.findById(userId);
            if (!user) {
                return res.status(404).json({ msg: 'User not found!' });
            }
            user.avatar = avatarUrl;
            await user.save();

            res.json({ msg: 'Avatar updated successfully', avatar: avatarUrl });
        })
    } catch (error) {
        res.status(500).json({ msg: 'Server error!' });
        console.log(error);
    }
}
