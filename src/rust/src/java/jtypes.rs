use core::slice;
use crate::core::signaling;
use std::fmt;


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
pub struct JArrayByte {
  pub len: usize,
  pub data: [u8; 256],
}

impl fmt::Display for JArrayByte {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "JArrayByte with {} bytes at {:?}", self.len, &(self.data))
    }
}
impl JArrayByte {
    pub fn new(vector: Vec<u8> ) -> Self {
        let vlen = vector.len();
        let mut vdata= [0; 256];
        for i in 0..vlen {
            vdata[i] = vector[i];
        }
        JArrayByte{len:vlen, data:vdata}
    }   

    pub fn empty() -> Self {
        let data = [0;256];
        JArrayByte{len: 0, data: data}
    }   

}


#[repr(C)]
#[derive(Debug)]
pub struct JArrayByte2D {
    pub len: usize,
    pub data: [u8; 256],
    // pub data: [JArrayByte;25],
}

impl JArrayByte2D {
    pub fn new(vector: Vec<signaling::IceCandidate>) -> Self {
info!("I have to create a jArrayByte with {} elements" , vector.len());
        let vlen = vector.len();
        let mut myrows: [JArrayByte; 25] = [JArrayByte::empty(); 25];
        for i in 0..25 {
            if (i < vlen) {
                myrows[i] = JArrayByte::new(vector[i].opaque.clone());
                // myrows[i] = JByteArray::from_data(vector[i].opaque.as_ptr(), vector[i].opaque.len());
info!("IceVec[{}] = {:?}", i, vector[i].opaque);
            } else {
                // myrows[i] = JByteArray::new(Vec::new());
                myrows[i] = JArrayByte::new(Vec::new());
            }
info!("Myrow[{}] : {}", i, myrows[i]);
        }
info!("data at {:?}", myrows);
        JArrayByte2D {
            len: vlen,
            data: [1;256]
        }
    }   
}


#[repr(C)]
#[derive(Clone, Copy, Debug)]
pub struct JByteArray {
    len: usize,
    pub buff: *const u8,
}

impl fmt::Display for JByteArray {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        let address = &self.buff;
        write!(f, "jByteArray with {} bytes at {:p}", self.len, self.buff)
    }
}

impl JByteArray {
    pub fn new(vector: Vec<u8>) -> Self {
        let slice = vector.as_slice();
        let buffer = slice.as_ptr();
        JByteArray{len: vector.len(), buff: buffer}
    }

    pub fn to_vec_u8(&self) -> Vec<u8> {
        let answer = unsafe { slice::from_raw_parts(self.buff, self.len).to_vec() };
        answer
    }

    pub fn empty() -> Self {
        let bar = Vec::new().as_ptr();
        JByteArray{len: 0, buff: bar}
    }

    pub fn from_data(data: *const u8, len: usize) -> Self {
        JByteArray{len: len, buff: data}
    }

}

#[repr(C)]
#[derive(Debug)]
pub struct JByteArray2D {
    pub len: usize,
    pub buff: [JByteArray;32],
}

impl JByteArray2D {
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
}
