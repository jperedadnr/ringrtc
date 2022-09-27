use core::slice;
use crate::core::signaling;


#[repr(C)]
#[derive(Debug)]
pub struct JString {
    len: usize,
    buff: *mut u8,
}


impl JString {
    pub fn to_string(&self) -> String {
        let answer = unsafe { String::from_raw_parts(self.buff, self.len, self.len) };
        answer
    }
}

#[repr(C)]
#[derive(Clone, Copy, Debug)]
pub struct JByteArray {
    len: usize,
    buff: *mut u8,
}


impl JByteArray {
    pub fn new(mut vector: Vec<u8>) -> Self {
        let buffer = vector.as_mut_ptr();
        JByteArray{len: vector.len(), buff: buffer}
    }

    pub fn to_vec_u8(&self) -> Vec<u8> {
        let answer = unsafe { slice::from_raw_parts(self.buff, self.len).to_vec() };
        answer
    }

    pub fn empty() -> Self {
        let bar = Vec::new().as_mut_ptr();
        JByteArray{len: 0, buff: bar}
    }

/*
    pub fn from_data(data: *mut u8, len: usize) -> Self {
        JByteArray{len: len, buff: data}
    }
*/

}

#[repr(C)]
#[derive(Debug)]
pub struct JByteArray2D {
    pub len: usize,
    pub buff: [JByteArray;32],
}

impl JByteArray2D {
/*
    pub fn new(vector: Vec<signaling::IceCandidate>) -> Self {
        let vlen = vector.len();
        // let mut myrows = [Opaque::empty(); 25];
        let mut myrows: [JByteArray; 32] = [JByteArray::empty(); 32];
        for i in 0..25 {
            if (i < vlen) {
                myrows[i] = JByteArray::from_data(vector[i].opaque.as_ptr(), vector[i].opaque.len());
            } else {
                myrows[i] = JByteArray::new(Vec::new());
            }
        }
        JByteArray2D {
            len: vlen,
            buff: myrows,
        }
    }   
*/
}
