package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.util.SerialNumber

class HardwareMapEX(@PublishedApi internal val hw: HardwareMap) : Iterable<HardwareDevice> by hw {
    inline operator fun <reified T: HardwareDevice> get(name: String): T = hw[T::class.java, name]
    inline operator fun <reified T: HardwareDevice> get(serial: SerialNumber): T? = hw[T::class.java, serial]
    operator fun set(name: String, device: HardwareDevice) = hw.put(name, device)
    operator fun set(serial: SerialNumber, name: String, device: HardwareDevice) = hw.put(serial, name, device)
    val size get() = hw.size()
//    inline fun deviceMapping
}