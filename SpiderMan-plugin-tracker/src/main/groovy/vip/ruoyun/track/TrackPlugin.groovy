package vip.ruoyun.track

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import vip.ruoyun.track.bean.AutoClassFilter
import vip.ruoyun.track.util.AutoTextUtil
import vip.ruoyun.track.bean.AutoSettingParams
import vip.ruoyun.track.asm.AutoTransform
import vip.ruoyun.track.util.Logger
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 插件入口
 */
class TrackPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def param = project.extensions.create('spiderMan', AutoSettingParams)
        GlobalConfig.setProject(project)
        println(GlobalConfig.getParams().name)

        //使用Transform实行遍历
        def android = project.extensions.getByType(AppExtension)
        registerTransform(android)

        project.afterEvaluate {
            Logger.setDebug(param.isDebug)
            // 用户配置解析
            analysiUserConfig()
        }
    }

    def static registerTransform(BaseExtension android) {
        AutoTransform transform = new AutoTransform()
        android.registerTransform(transform)
    }

    /**
     * 对build.gradle配置参数及自定义内容进行解析
     */
    static void analysiUserConfig() {

        List<Map<String, Object>> matchDataList = GlobalConfig.getParams().matchData
        List<AutoClassFilter> autoClassFilterList = new ArrayList<>()

        matchDataList.each {
            Map<String, Object> map ->
                AutoClassFilter classFilter = new AutoClassFilter()

                String className = map.get("ClassName")
                String interfaceName = map.get("InterfaceName")
                String methodName = map.get("MethodName")
                String methodDes = map.get("MethodDes")
                Closure methodVisitor = map.get("MethodVisitor")
                boolean isAnnotation = map.get("isAnnotation")
                // 类的全类名
                if (!AutoTextUtil.isEmpty(className)) {
                    className = AutoTextUtil.changeClassNameSeparator(className)
                }
                // 接口的全类名
                if (!AutoTextUtil.isEmpty(interfaceName)) {
                    interfaceName = AutoTextUtil.changeClassNameSeparator(interfaceName)
                }

                classFilter.setClassName(className)
                classFilter.setInterfaceName(interfaceName)
                classFilter.setMethodName(methodName)
                classFilter.setMethodDes(methodDes)
                classFilter.setMethodVisitor(methodVisitor)
                classFilter.setIsAnnotation(isAnnotation)

                autoClassFilterList.add(classFilter)
        }

        GlobalConfig.setAutoClassFilter(autoClassFilterList)

        // 需要手动添加的包
        List<String> includePackages = GlobalConfig.getParams().include
        HashSet<String> include = new HashSet<>()
        if (includePackages != null) {
            include.addAll(includePackages)
        }
        GlobalConfig.setInclude(include)

        // 添加需要过滤的包
        List<String> excludePackages = GlobalConfig.getParams().exclude
        HashSet<String> exclude = new HashSet<>()
        // 不对系统类进行操作，避免非预期错误
        exclude.add('android.support')
        exclude.add('androidx')
        if (excludePackages != null) {
            exclude.addAll(excludePackages)
        }
        GlobalConfig.setExclude(exclude)
    }

}