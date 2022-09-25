use crate::webrtc::logging;

fn init_logging() {
    env_logger::builder()
        .filter(None, log::LevelFilter::Debug)
        .init();
    println!("LOGINIT done");
    // let is_first_time_initializing_logger = log::set_logger(&LOG).is_ok();
    let is_first_time_initializing_logger = true;
    println!("EXTRALOG? {}", is_first_time_initializing_logger);
    if is_first_time_initializing_logger {
        // log::set_max_level(log::LevelFilter::Debug);
        logging::set_logger(log::LevelFilter::Warn);
        println!("EXTRALOG? yes");
    }
    // logging::set_logger(log::LevelFilter::Trace);
    info!("INFO logging enabled");
}

#[no_mangle]
pub unsafe extern "C" fn initRingRTC() -> i64 {
    println!("Initialize RingRTC, init logging");
    init_logging();
    info!("Initialized RingRTC, using logging");
    1   
}
