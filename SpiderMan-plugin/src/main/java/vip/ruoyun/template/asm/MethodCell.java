package vip.ruoyun.template.asm;

import java.util.List;

public class MethodCell {

    // 原方法名
    final String name;

    // 原方法描述
    final String desc;

    // 方法所在的接口或类
    final String parent;

    // 采集数据的方法名
    final String agentName;

    // 采集数据的方法描述
    final String agentDesc;

    // 采集数据的方法参数起始索引（ 0：this，1+：普通参数 ）
    final int paramsStart;

    // 采集数据的方法参数个数
    final int paramsCount;

    // 参数类型对应的ASM指令，加载不同类型的参数需要不同的指令
    final List<Integer> opcodes;

    MethodCell(String name, String desc, String parent, String agentName, String agentDesc, int paramsStart,
            int paramsCount, List<Integer> opcodes) {
        this.name = name;
        this.desc = desc;
        this.parent = parent;
        this.agentName = agentName;
        this.agentDesc = agentDesc;
        this.paramsStart = paramsStart;
        this.paramsCount = paramsCount;
        this.opcodes = opcodes;
    }
}
