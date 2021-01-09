package dev.onyxstudios.refinedmachinery.client.shaders;

import dev.onyxstudios.refinedmachinery.RefinedMachinery;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL30.*;

public abstract class ShaderProgram {

    private FloatBuffer matrixBuffer;
    protected ResourceLocation vertShader;
    protected ResourceLocation fragShader;

    protected int programId;
    protected int vertexShaderId;
    protected int fragmentShaderId;

    public ShaderProgram(ResourceLocation vertShader, ResourceLocation fragShader) {
        this.vertShader = vertShader;
        this.fragShader = fragShader;
    }

    public void init() {
        matrixBuffer = BufferUtils.createFloatBuffer(16);
        programId = glCreateProgram();

        if(vertShader != null) {
            vertexShaderId = createShader(vertShader, GL_VERTEX_SHADER);
            glAttachShader(programId, vertexShaderId);
        }

        if(fragShader != null) {
            fragmentShaderId = createShader(fragShader, GL_FRAGMENT_SHADER);
            glAttachShader(programId, fragmentShaderId);
        }

        bindAttributes();
        glLinkProgram(programId);
        glValidateProgram(programId);
        this.getAllUniformLocations();
    }

    public void start() {
        glUseProgram(programId);
    }

    public void stop () {
        glUseProgram(0);
    }

    public abstract void getAllUniformLocations();

    public int getUniformLocation(String uniformName) {
        return glGetUniformLocation(programId, uniformName);
    }

    public void bindAttributes() {
    }

    protected void bindAttribute(int attribute, String variableName) {
        glBindAttribLocation(programId, attribute, variableName);
    }

    protected void loadFloat(int location, float value) {
        glUniform1f(location, value);
    }

    protected void loadInt(int location, int value) {
        glUniform1i(location, value);
    }

    protected void loadVector(int location, Vector3f vector) {
        glUniform3f(location, vector.x, vector.y, vector.z);
    }

    protected void loadVector(int location, Vector4f vector) {
        glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
    }

    protected void loadMatrix(int location, Matrix4f matrix) {
        matrix.get(matrixBuffer);
        glUniformMatrix4fv(location, false, matrixBuffer);
    }

    private int createShader(ResourceLocation location, int shaderType) {
        int shader = glCreateShader(shaderType);
        if(shader == 0) return 0;

        glShaderSource(shader, readFileAsString(location));
        glCompileShader(shader);
        if(glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            RefinedMachinery.LOGGER.fatal("Could not compile shader!");
            throw new RuntimeException("Error creating shader: " + glGetShaderInfoLog(shader));
        }

        return shader;
    }

    private String readFileAsString(ResourceLocation location) {
        InputStream shaderStream = getShaderStream(location);
        String s = "";

        if(shaderStream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(shaderStream, "UTF-8"))) {
                s = reader.lines().collect(Collectors.joining("\n"));
            } catch (IOException e) {
                RefinedMachinery.LOGGER.fatal("Unable to read shader file! Source: " + location.toString(), e);
            }
        }

        return s;
    }

    private InputStream getShaderStream(ResourceLocation location) {
        if(Minecraft.getInstance().getResourceManager().hasResource(location)) {
            try {
                return Minecraft.getInstance().getResourceManager().getResource(location).getInputStream();
            }catch (IOException e) {
                RefinedMachinery.LOGGER.fatal("Unable to read shader file! Source: " + location.toString(), e);
                return null;
            }
        }else {
            RefinedMachinery.LOGGER.fatal("Unable to locate shader file! Source: " + location.toString());
            return null;
        }
    }
}
