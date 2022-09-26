use core::slice;
use crate::native::PeerId;

#[repr(C)]
#[derive(Debug)]
pub struct JString {
    len: usize,
    buff: *mut u8,
}


impl JString {
    pub fn toString(&self) -> String {
        let answer = unsafe { String::from_raw_parts(self.buff, self.len, self.len) };
        answer
    }
}

#[repr(C)]
#[derive(Debug)]
pub struct JByteArray {
    len: usize,
    buff: *mut u8,
}


impl JByteArray {
    pub fn toVecU8(&self) -> Vec<u8> {
        let answer = unsafe { slice::from_raw_parts(self.buff, self.len).to_vec() };
        answer
    }
}
