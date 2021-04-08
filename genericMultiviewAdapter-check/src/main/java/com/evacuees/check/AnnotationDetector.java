package com.evacuees.check;

import com.android.SdkConstants;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.support.AndroidxName;
import com.android.tools.lint.checks.ApiDetector;
import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.client.api.JavaEvaluatorKt;
import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Lint;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.SourceCodeScanner;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiArrayType;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;

import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UDeclaration;
import org.jetbrains.uast.UDeclarationsExpression;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UExpression;
import org.jetbrains.uast.ULiteralExpression;
import org.jetbrains.uast.ULocalVariable;
import org.jetbrains.uast.UMethod;
import org.jetbrains.uast.UNamedExpression;
import org.jetbrains.uast.UParameter;
import org.jetbrains.uast.USwitchExpression;
import org.jetbrains.uast.UVariable;
import org.jetbrains.uast.UastUtils;
import org.jetbrains.uast.util.UastExpressionUtils;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("UnstableApiUsage")
public class AnnotationDetector extends Detector implements SourceCodeScanner {

    public static final AndroidxName RESTRICT_TO_ANNOTATION =
            AndroidxName.of(SdkConstants.SUPPORT_ANNOTATIONS_PREFIX, "RestrictTo");

    public static final String GRecyclerViewFactory_ANNOTATION = "com.dilwar.annotations.GRecyclerViewFactory";

    public static final Implementation IMPLEMENTATION =
            new Implementation(AnnotationDetector.class, Scope.JAVA_FILE_SCOPE);

    public static final Issue INSIDE_METHOD =
            Issue.create(
                    "LocalSuppress",
                    "@SuppressLint on invalid element",
                    "The `@SuppressAnnotation` is used to suppress Lint warnings in Java files. However, "
                            + "while many lint checks analyzes the Java source code, where they can find "
                            + "annotations on (for example) local variables, some checks are analyzing the "
                            + "`.class` files. And in class files, annotations only appear on classes, fields "
                            + "and methods. Annotations placed on local variables disappear. If you attempt "
                            + "to suppress a lint error for a class-file based lint check, the suppress "
                            + "annotation not work. You must move the annotation out to the surrounding method.",
                    Category.CORRECTNESS,
                    3,
                    Severity.ERROR,
                    IMPLEMENTATION);

    /**
     * Incorrectly using a support annotation
     */
    @SuppressWarnings("WeakerAccess")
    public static final Issue ANNOTATION_USAGE =
            Issue.create(
                    "SupportAnnotationUsage",
                    "Incorrect support annotation usage",
                    "This lint check makes sure that the support annotations (such as "
                            + "`@IntDef` and `@ColorInt`) are used correctly. For example, it's an "
                            + "error to specify an `@IntRange` where the `from` value is higher than "
                            + "the `to` value.",
                    Category.CORRECTNESS,
                    2,
                    Severity.ERROR,
                    IMPLEMENTATION);


    /**
     * Constructs a new {@link AnnotationDetector} check
     */
    public AnnotationDetector() {
    }

    /**
     * Returns the node to use as the scope for the given annotation node. You can't annotate an
     * annotation itself (with {@code @SuppressLint}), but you should be able to place an annotation
     * next to it, as a sibling, to only suppress the error on this annotated element, not the whole
     * surrounding class.
     */
    @NonNull
    private static UElement getAnnotationScope(@NonNull UAnnotation node) {
        UElement scope = UastUtils.getParentOfType(node, UAnnotation.class, true);
        if (scope == null) {
            scope = node;
        }
        return scope;
    }

    @Override
    public List<Class<? extends UElement>> getApplicableUastTypes() {
        List<Class<? extends UElement>> types = new ArrayList<>(2);
        types.add(UAnnotation.class);
        types.add(USwitchExpression.class);
        return types;
    }

    @Nullable
    @Override
    public UElementHandler createUastHandler(@NonNull JavaContext context) {
        return new AnnotationChecker(context);
    }

    private class AnnotationChecker extends UElementHandler {
        private final JavaContext mContext;

        public AnnotationChecker(JavaContext context) {
            mContext = context;
        }

        @Override
        public void visitAnnotation(@NonNull UAnnotation annotation) {
            String type = annotation.getQualifiedName();
            if (type == null || type.startsWith("java.lang.")) {
                return;
            }

            if (SdkConstants.FQCN_SUPPRESS_LINT.equals(type)) {
                UElement parent = annotation.getUastParent();
                if (parent == null) {
                    return;
                }
                // Only flag local variables and parameters (not classes, fields and methods)
                if (!(parent instanceof UDeclarationsExpression
                        || parent instanceof ULocalVariable
                        || parent instanceof UParameter)) {
                    return;
                }
                List<UNamedExpression> attributes = annotation.getAttributeValues();
                if (attributes.size() == 1) {
                    UNamedExpression attribute = attributes.get(0);
                    UExpression value = attribute.getExpression();
                    if (value instanceof ULiteralExpression) {
                        Object v = ((ULiteralExpression) value).getValue();
                        if (v instanceof String) {
                            String id = (String) v;
                            checkSuppressLint(annotation, id);
                        }
                    } else if (UastExpressionUtils.isArrayInitializer(value)) {
                        for (UExpression ex : ((UCallExpression) value).getValueArguments()) {
                            if (ex instanceof ULiteralExpression) {
                                Object v = ((ULiteralExpression) ex).getValue();
                                if (v instanceof String) {
                                    String id = (String) v;
                                    if (!checkSuppressLint(annotation, id)) {
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (RESTRICT_TO_ANNOTATION.isEquals(type)) {
                UExpression attributeValue = annotation.findDeclaredAttributeValue(SdkConstants.ATTR_VALUE);
                if (attributeValue == null) {
                    attributeValue = annotation.findDeclaredAttributeValue(null);
                }
                if (attributeValue == null) {
                    mContext.report(
                            ANNOTATION_USAGE,
                            annotation,
                            mContext.getLocation(annotation),
                            "Restrict to what? Expected at least one `RestrictTo.Scope` arguments.");
                } else {
                    String values = attributeValue.asSourceString();
                    if (values.contains("SUBCLASSES")
                            && annotation.getUastParent() instanceof UClass) {
                        mContext.report(
                                ANNOTATION_USAGE,
                                annotation,
                                mContext.getLocation(annotation),
                                "`RestrictTo.Scope.SUBCLASSES` should only be specified on methods and fields");
                    }

                }
            } else if (RESTRICT_TO_ANNOTATION.isEquals(type)) {
                UExpression attributeValue = annotation.findDeclaredAttributeValue(SdkConstants.ATTR_VALUE);
                if (attributeValue == null) {
                    attributeValue = annotation.findDeclaredAttributeValue(null);
                }
                if (attributeValue == null) {
                    mContext.report(
                            ANNOTATION_USAGE,
                            annotation,
                            mContext.getLocation(annotation),
                            "Restrict to what? Expected at least one `RestrictTo.Scope` arguments.");
                } else {
                    String values = attributeValue.asSourceString();
                    if (values.contains("SUBCLASSES")
                            && annotation.getUastParent() instanceof UClass) {
                        mContext.report(
                                ANNOTATION_USAGE,
                                annotation,
                                mContext.getLocation(annotation),
                                "`RestrictTo.Scope.SUBCLASSES` should only be specified on methods and fields");
                    }

                }
            } else {
                // Look for typedefs (and make sure they're specified on the right type)
                PsiElement resolved = annotation.resolve();
                if (resolved != null) {
                    PsiClass cls = (PsiClass) resolved;
                    if (cls.isAnnotationType() && cls.getModifierList() != null) {
                        for (PsiAnnotation a :
                                mContext.getEvaluator().getAllAnnotations(cls, false)) {
                            String name = a.getQualifiedName();
                            if (SdkConstants.INT_DEF_ANNOTATION.isEquals(name)) {
                                checkTargetType(annotation, JavaEvaluatorKt.TYPE_INT, JavaEvaluatorKt.TYPE_LONG, true);
                            } else if (SdkConstants.LONG_DEF_ANNOTATION.isEquals(name)) {
                                checkTargetType(annotation, JavaEvaluatorKt.TYPE_LONG, null, true);
                            } else if (SdkConstants.STRING_DEF_ANNOTATION.isEquals(type)) {
                                checkTargetType(annotation, JavaEvaluatorKt.TYPE_STRING, null, true);
                            }
                        }
                    }
                }
            }
        }

        private void checkTargetType(
                @NonNull UAnnotation node,
                @NonNull String type1,
                @Nullable String type2,
                boolean allowCollection) {
            checkTargetType(node, type1, type2, null, null, allowCollection);
        }

        private void checkTargetType(
                @NonNull UAnnotation node,
                @NonNull String type1,
                @Nullable String type2,
                @Nullable String type3,
                @Nullable String type4,
                boolean allowCollection) {
            UElement parent = node.getUastParent();
            PsiType type;

            if (parent instanceof UDeclarationsExpression) {
                List<UDeclaration> elements = ((UDeclarationsExpression) parent).getDeclarations();
                if (!elements.isEmpty()) {
                    UDeclaration element = elements.get(0);
                    if (element instanceof ULocalVariable) {
                        type = ((ULocalVariable) element).getType();
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else if (parent instanceof UMethod) {
                UMethod method = (UMethod) parent;
                type =
                        method.isConstructor()
                                ? mContext.getEvaluator()
                                .getClassType(UastUtils.getContainingUClass(method))
                                : method.getReturnType();
            } else if (parent instanceof UVariable) {
                // Field or local variable or parameter
                UVariable variable = (UVariable) parent;
                if (variable.getTypeReference() == null) {
                    // Uh oh.
                    // https://youtrack.jetbrains.com/issue/KT-20172
                    return;
                }
                type = variable.getType();
            } else {
                return;
            }
            if (type == null) {
                return;
            }

            if (allowCollection) {
                if (type instanceof PsiArrayType) {
                    // For example, int[]
                    type = type.getDeepComponentType();
                } else if (type instanceof PsiClassType) {
                    // For example, List<Integer>
                    PsiClassType classType = (PsiClassType) type;
                    if (classType.getParameters().length == 1) {
                        PsiClass resolved = classType.resolve();
                        if (resolved != null
                                && mContext.getEvaluator()
                                .implementsInterface(
                                        resolved, "java.util.Collection", false)) {
                            type = classType.getParameters()[0];
                        }
                    }
                }
            }

            if (!type.isValid()) {
                return;
            }

            String typeName = type.getCanonicalText();
            if (typeName.equals("error.NonExistentClass")) {
                // Type not found. Not awesome.
                // https://youtrack.jetbrains.com/issue/KT-20172
                return;
            }

            if (!(typeName.equals(type1)
                    || typeName.equals(type2)
                    || typeName.equals(type3)
                    || typeName.equals(type4))) {
                // Autoboxing? You can put @DrawableRes on a java.lang.Integer for example
                if (typeName.equals(Lint.getAutoBoxedType(type1))
                        || type2 != null && typeName.equals(Lint.getAutoBoxedType(type2))
                        || type3 != null && typeName.equals(Lint.getAutoBoxedType(type3))
                        || type4 != null && typeName.equals(Lint.getAutoBoxedType(type4))) {
                    return;
                }

                String expectedTypes;
                if (type4 != null) {
                    expectedTypes = type1 + ", " + type2 + ", " + type3 + ", or " + type4;
                } else if (type3 != null) {
                    expectedTypes = type1 + ", " + type2 + ", or " + type3;
                } else if (type2 != null) {
                    expectedTypes = type1 + " or " + type2;
                } else {
                    expectedTypes = type1;
                }
                if (typeName.equals(JavaEvaluatorKt.TYPE_STRING)) {
                    typeName = "String";
                }
                String message =
                        String.format(
                                "This annotation does not apply for type %1$s; expected %2$s",
                                typeName, expectedTypes);
                Location location = mContext.getLocation(node);
                mContext.report(ANNOTATION_USAGE, node, location, message);
            }
        }


        private boolean checkSuppressLint(@NonNull UAnnotation node, @NonNull String id) {
            IssueRegistry registry = mContext.getDriver().getRegistry();
            Issue issue = registry.getIssue(id);
            // Special-case the ApiDetector issue, since it does both source file analysis
            // only on field references, and class file analysis on the rest, so we allow
            // annotations outside of methods only on fields
            if (issue != null && !issue.getImplementation().getScope().contains(Scope.JAVA_FILE)
                    || issue == ApiDetector.UNSUPPORTED) {
                // This issue doesn't have AST access: annotations are not
                // available for local variables or parameters
                UElement scope = getAnnotationScope(node);
                mContext.report(
                        INSIDE_METHOD,
                        scope,
                        mContext.getLocation(node),
                        String.format(
                                "The `@SuppressLint` annotation cannot be used on a local "
                                        + "variable with the lint check '%1$s': move out to the "
                                        + "surrounding method",
                                id));
                return false;
            }

            return true;
        }

    }

}
