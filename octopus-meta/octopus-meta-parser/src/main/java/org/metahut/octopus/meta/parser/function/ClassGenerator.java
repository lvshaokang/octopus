package org.metahut.octopus.meta.parser.function;

import org.codehaus.commons.compiler.CompilerFactoryFactory;
import org.codehaus.commons.compiler.ICompiler;
import org.codehaus.commons.compiler.util.resource.MapResourceCreator;
import org.codehaus.commons.compiler.util.resource.Resource;
import org.codehaus.commons.compiler.util.resource.StringResource;
import org.metahut.octopus.meta.parser.domain.SymbolConstants;
import org.metahut.octopus.meta.parser.domain.model.AbstractStructModel;
import org.metahut.octopus.meta.parser.domain.model.ClassModel;
import org.metahut.octopus.meta.parser.domain.enums.RelType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * import constant pool
 */
public class ClassGenerator {

    public static final void load(String classInfo) throws Exception {
        ICompiler compiler = CompilerFactoryFactory.getDefaultCompilerFactory(ClassGenerator.class.getClassLoader()).newCompiler();

        // Store generated .class files in a Map:
        Map<String, byte[]> classes = new HashMap<String, byte[]>();
        compiler.setClassFileCreator(new MapResourceCreator(classes));

        // Now compile two units from strings:
        compiler.compile(new Resource[]{
            new StringResource(
                    "octopus/model/HiveModel.java",
                    "package octopus.model;\n"
                            + "import octopus.attribute.HiveAttribute;\n"
                            + "public class HiveModel {\n"
                            + "\t\tprivate HiveAttribute hiveAttribute;\n"
                            + "\t\tpublic void setHiveAttribute(HiveAttribute hiveAttribute) {\n"
                            + "\t\t\t\tthis.hiveAttribute = hiveAttribute;\n"
                            + "\t\t}\n"
                            + "\t\tpublic HiveAttribute getHiveAttribute(){\n"
                            + "\t\t\t\treturn this.hiveAttribute;\n"
                            + "\t\t}\n"
                            + "}\n"
            ),
            new StringResource(
                    "octopus/attribute/HiveAttribute.java",
                    "package octopus.attribute;\n"
                            + "public class HiveAttribute {"
                            + "\t\tprivate String tableName;\n"
                            + "\t\tpublic void setTableName(String tableName) {\n"
                            + "\t\t\t\tthis.tableName = tableName;\n"
                            + "\t\t}\n"
                            + "\t\tpublic String getTableName(){\n"
                            + "\t\t\t\treturn this.tableName;\n"
                            + "\t\t}\n"
                            + "}\n"
            ),
        });
    }

    /**
     * 1.link -> do how
     * 2.uid ?
     * 3.list (complete)
     * 4.graph ?  需要个解决依赖的方式
     * 5.封装 ？ 定义 rel ? 图 ？
     * @param env
     * @param model
     * @return
     */
    public static final String toClassFile(String env, AbstractStructModel model) {
        if (model instanceof ClassModel) {
            ClassModel classModel = (ClassModel)model;
            LineStringBuilder packageBuilder = new LineStringBuilder();
            packageBuilder.appendLine("package ",env,".",model.getPackagePath(),";\n");
            LineStringBuilder importBuilder = new LineStringBuilder();
            LineStringBuilder classNameBuilder = new LineStringBuilder()
                    .appendLine("public class ",classModel.getName()," {\n");
            LineStringBuilder attributesBuilder = new LineStringBuilder();
            attributesBuilder.appendLine(SymbolConstants.INDENT,"private static final long ",String.valueOf(model.getSerialVersionUID()),"L", SymbolConstants.LINE_TAIL);
            Set<String> imports = new HashSet<>();
            //attribute -> import how o（n）
            classModel.getAttributeModels()
                    .stream()
                    .filter(attributeModel -> attributeModel != null
                            && attributeModel.getClassName() != null)
                    .forEach(attributeModel -> {
                        int index = attributeModel.getName().lastIndexOf(SymbolConstants.PACKAGE_SPLIT);
                        if (attributeModel.isArray() && imports.add(SymbolConstants.LIST_IMPORT)) {
                            importBuilder.appendLine(SymbolConstants.LIST_IMPORT);
                        }
                        // import Class
                        String simpleClassName;
                        if (index != -1) {
                            String preName = "";
                            if (RelType.CUSTOM == attributeModel.getRelType()) {
                                preName = env + SymbolConstants.PACKAGE_SPLIT;
                            }
                            importBuilder.appendLine(SymbolConstants.IMPORT,preName,attributeModel.getClassName(), SymbolConstants.LINE_TAIL);
                            simpleClassName = attributeModel.getClassName().substring(index + 1);
                        } else {
                            simpleClassName = attributeModel.getClassName();
                        }
                        // addAttribute
                        if (attributeModel.isArray()) {
                            attributesBuilder.appendLine(SymbolConstants.INDENT,"List<",simpleClassName,">",attributeModel.getName(), SymbolConstants.LINE_TAIL);
                        } else {
                            attributesBuilder.appendLine(SymbolConstants.INDENT,simpleClassName,attributeModel.getName(), SymbolConstants.LINE_TAIL);
                        }
                    });
            return new LineStringBuilder()
                    .union(packageBuilder)
                    .union(importBuilder)
                    .union(classNameBuilder)
                    .union(attributesBuilder)
                    .append('}')
                    .toString();
        } else {
            SymbolConstants.LOG.error("No support Model type[{}]", model.getClass());
        }
        return null;
    }

}
