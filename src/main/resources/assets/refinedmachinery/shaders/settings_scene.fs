#version 330

in vec2 pass_textureCoords;

out vec4 fragColor;

uniform sampler2D textureSampler;
uniform float alpha;

void main() {
    vec4 color = texture(textureSampler, pass_textureCoords);
    fragColor = vec4(color.r, color.g, color.b, alpha * color.a);
}