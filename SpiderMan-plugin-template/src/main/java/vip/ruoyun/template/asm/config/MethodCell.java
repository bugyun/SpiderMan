package vip.ruoyun.template.asm.config;

import java.util.Arrays;

public class MethodCell {

    //value.name
    //value.desc
    //
    //opcode 0
    //
    //植入的方法
    //owner vip/ruoyun/track/core/SpiderManTracker
    //name
    //desc
    //maxStack
    //maxLocals

    // methodCell.name = "onDestroy";
    //                    methodCell.desc = "()V";
    //                    methodCell.opcodes = {ALOAD};
    //                    methodCell.parent = superName;
    //                    methodCell.agentName = "vip/ruoyun/track/core/SpiderManTracker";
    //                    methodCell.agentDesc = "(L" + superName + ";)V";
    //                    methodCell.paramsStart = 0;
    //                    methodCell.paramsCount = 1;

    // 原方法名
    public final String name;

    // 原方法描述
    public final String desc;

    // 采集数据的方法名
    public final String agentName;

    // 采集数据的方法描述
    public final String agentDesc;

    // 采集数据的方法参数起始索引（ 0：this，1+：普通参数 ）
    public final int paramsStart;

    // 采集数据的方法参数个数
    public final int paramsCount;

    // 参数类型对应的ASM指令，加载不同类型的参数需要不同的指令
    public final int[] opcodes;

    public MethodCell(String name, String desc, String agentName, String agentDesc, int paramsStart,
            int paramsCount, int... opcodes) {
        this.name = name;
        this.desc = desc;
        this.agentName = agentName;
        this.agentDesc = agentDesc;
        this.paramsStart = paramsStart;
        this.paramsCount = paramsCount;
        this.opcodes = opcodes;
    }

    @Override
    public String toString() {
        return "MethodCell{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", agentName='" + agentName + '\'' +
                ", agentDesc='" + agentDesc + '\'' +
                ", paramsStart=" + paramsStart +
                ", paramsCount=" + paramsCount +
                ", opcodes=" + Arrays.toString(opcodes) +
                '}';
    }
}
